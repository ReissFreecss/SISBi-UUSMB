package jsf;

import static com.sun.faces.facelets.util.Path.context;
import jpa.entities.SampleGenomeLink;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.SampleGenomeLinkFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.Genome;
import jpa.entities.Sample;

@Named("sampleGenomeLinkController")
@SessionScoped
public class SampleGenomeLinkController implements Serializable {

    private SampleGenomeLink current;
    private SampleGenomeLink currentOrg;
    private SampleGenomeLink currentCont;
    private SampleGenomeLink currentRef;
    private String org;
    private String cont;
    List<String> listCont;
    private String ref;

    private DataModel items = null;
    @EJB
    private jpa.session.SampleGenomeLinkFacade ejbFacade;
    @EJB
    private jpa.session.SampleFacade sampleFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private boolean haveOrg = true;
    private boolean haveref;
    private boolean haveCont = false;
    private boolean haveBioAn = true;
    private boolean canCreate = false;

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isHaveBioAn() {
        return haveBioAn;
    }

    public void setHaveBioAn(boolean haveBioAn) {
        this.haveBioAn = haveBioAn;
    }

    public List<String> getListCont() {
        return listCont;
    }

    public void setListCont(List<String> listCont) {
        this.listCont = listCont;
    }

    public boolean isHaveref() {
        return haveref;
    }

    public void setHaveref(boolean haveref) {
        this.haveref = haveref;
    }

    public boolean isHaveOrg() {
        return haveOrg;
    }

    public void setHaveOrg(boolean haveOrg) {
        this.haveOrg = haveOrg;
    }

    public void change() {

        if (org != null) {
            List<Genome> list = ejbFacade.findAllGenomes();
            Genome aux = null;
            for (Genome genome : list) {

                if (genome.toString().equals(org)) {
                    aux = genome;
                }
            }
            if (aux != null) {
                canCreate = false;
                haveref = aux.getIdGenome().startsWith("GCF");

            } else {
                canCreate = true;
            }

        } else {
            canCreate = false;
        }
    }

    public void changeCont() {

        haveCont = !haveCont;

    }

    public boolean isHaveCont() {

        return haveCont;
    }

    public void setHaveCont(boolean haveCont) {
        this.haveCont = haveCont;
    }

    public String getOrg() {

        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public SampleGenomeLinkController() {
    }

    public SampleGenomeLink getSelected1() {
        if (currentOrg == null) {

            currentOrg = new SampleGenomeLink();
            currentOrg.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());

        }
        return currentOrg;
    }

    public SampleGenomeLink getSelected2() {
        if (currentRef == null) {
            currentRef = new SampleGenomeLink();
            currentRef.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
            selectedItemIndex = -1;
        }
        return currentRef;
    }

    public SampleGenomeLink getSelected3() {
        if (currentCont == null) {
            currentCont = new SampleGenomeLink();
            currentCont.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
            selectedItemIndex = -1;
        }
        return currentCont;
    }

    public SampleGenomeLink getSelected() {
        if (current == null) {
            current = new SampleGenomeLink();
            current.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private SampleGenomeLinkFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (SampleGenomeLink) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {

        current = new SampleGenomeLink();
        current.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {

        try {

            createOrg();

            createRef();

            createCont();

            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "no se guardo XD " + e);
            System.out.println(e);
            return null;
        }
    }

    public void createOrg() {
        try {
            if (org != null) {

                currentOrg = new SampleGenomeLink();
                currentOrg.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());

                FacesContext context = FacesContext.getCurrentInstance();
                Sample sm = (Sample) context.getExternalContext().getSessionMap().get("sample");

                currentOrg.getSampleGenomeLinkPK().setIdSample(sm.getIdSample());
                List<Genome> lista = ejbFacade.findAllGenomes();
                String GenomeId = "";
                String GenoString = "";
                System.out.println("llega al for de createOrg");
                for (Genome genome : lista) {
                    if (genome.toString().equals(org)) {
                        GenomeId = genome.getIdGenome();
                        GenoString = genome.toString();
                    }
                }
                System.out.println(GenomeId);
                currentOrg.getSampleGenomeLinkPK().setIdGenome(GenomeId);

                currentOrg.getSampleGenomeLinkPK().setType("Organismo Fuente");
                getFacade().create(currentOrg);

                JsfUtil.addSuccessMessage("Organismo: " + GenoString);
            }

        } catch (Exception e) {

            System.out.println(e);

        }
    }

    public void createRef() {
        try {
            if (ref != null) {
                currentRef = new SampleGenomeLink();
                currentRef.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());

                FacesContext context = FacesContext.getCurrentInstance();
                Sample sm = (Sample) context.getExternalContext().getSessionMap().get("sample");

                currentRef.getSampleGenomeLinkPK().setIdSample(sm.getIdSample());
//chaos
                List<Genome> lista = ejbFacade.findAllGenomes();
                String GenomeId = "";
                String GenoString = "";
                for (Genome genome : lista) {
                    if (genome.toString().equals(ref)) {
                        GenomeId = genome.getIdGenome();
                        GenoString = genome.toString();
                    }
                }

                currentRef.getSampleGenomeLinkPK().setIdGenome(GenomeId);

                currentRef.getSampleGenomeLinkPK().setType("Referencia");
                getFacade().create(currentRef);

                JsfUtil.addSuccessMessage("Organismo de referencia: " + GenoString);
            }

        } catch (Exception e) {

            System.out.println(e);

        }
    }

    public void createCont() {
        try {

            if (listCont != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                Sample sm = (Sample) context.getExternalContext().getSessionMap().get("sample");

                List<Genome> lista = ejbFacade.findAllGenomes();

                String GenomeId = "";
                String contString = "";
                for (String gen : listCont) {
                    currentCont = new SampleGenomeLink();
                    currentCont.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                    currentCont.getSampleGenomeLinkPK().setIdSample(sm.getIdSample());

                    for (Genome genome : lista) {
                        if (genome.toString().equals(gen)) {
                            GenomeId = genome.getIdGenome();
                            contString = genome.toString();
                        }
                    }

                    currentCont.getSampleGenomeLinkPK().setIdGenome(GenomeId);

                    currentCont.getSampleGenomeLinkPK().setType("Contaminante");

                    getFacade().create(currentCont);

                    JsfUtil.addSuccessMessage("Contaminante: " + contString);

                }

            }

        } catch (Exception e) {

            System.out.println(e);

        }
    }

    public void editLinks(Sample sam) {

        editOrg(sam);
        editRef(sam);

    }

    public void editOrg(Sample sam) {
        try {
            if (org != null) {
                List<SampleGenomeLink> sglList = ejbFacade.findAll();
                ArrayList<SampleGenomeLink> someSGL = new ArrayList<>();

                for (SampleGenomeLink sampleGenomeLink : sglList) {
                    Integer a = sampleGenomeLink.getSampleGenomeLinkPK().getIdSample();

                    if (sam.getIdSample().toString().equals(a.toString())
                            && sampleGenomeLink.getSampleGenomeLinkPK().getType().equals("Organismo Fuente")) {
                        someSGL.add(sampleGenomeLink);
                    }

                }

                currentOrg = new SampleGenomeLink();
                currentOrg.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                currentOrg.getSampleGenomeLinkPK().setIdSample(sam.getIdSample());
                List<Genome> lista = ejbFacade.findAllGenomes();
                String GenomeId = "";

                for (Genome genome : lista) {
                    if (genome.toString().equals(org)) {
                        GenomeId = genome.getIdGenome();
                    }
                }
                System.out.println(GenomeId);
                currentOrg.getSampleGenomeLinkPK().setIdGenome(GenomeId);

                currentOrg.getSampleGenomeLinkPK().setType("Organismo Fuente");
                getFacade().create(currentOrg);

                if (!someSGL.isEmpty()) {
                    getFacade().remove(someSGL.get(0));

                }

            }
        } catch (Exception e) {

            System.out.println(e);

        }
        org = null;
    }

    public void editRef(Sample sam) {
        try {
            if (ref != null) {
                List<SampleGenomeLink> sglList = ejbFacade.findAll();
                ArrayList<SampleGenomeLink> someSGL = new ArrayList<>();

                for (SampleGenomeLink sampleGenomeLink : sglList) {
                    Integer a = sampleGenomeLink.getSampleGenomeLinkPK().getIdSample();

                    if (sam.getIdSample().toString().equals(a.toString())
                            && sampleGenomeLink.getSampleGenomeLinkPK().getType().equals("Referencia")) {
                        someSGL.add(sampleGenomeLink);
                    }

                }

                currentRef = new SampleGenomeLink();
                currentRef.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                currentRef.getSampleGenomeLinkPK().setIdSample(sam.getIdSample());
                List<Genome> lista = ejbFacade.findAllGenomes();
                String GenomeId = "";

                for (Genome genome : lista) {
                    if (genome.toString().equals(ref)) {
                        GenomeId = genome.getIdGenome();
                    }
                }
                System.out.println(GenomeId);
                currentRef.getSampleGenomeLinkPK().setIdGenome(GenomeId);

                currentRef.getSampleGenomeLinkPK().setType("Referencia");
                getFacade().create(currentRef);

                if (!someSGL.isEmpty()) {
                    getFacade().remove(someSGL.get(0));

                }

            }
        } catch (Exception e) {

            System.out.println(e);

        }
        ref = null;
    }

    public String prepareEdit() {
        current = (SampleGenomeLink) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getSampleGenomeLinkPK().setIdSample(current.getSample().getIdSample());
            current.getSampleGenomeLinkPK().setIdGenome(current.getGenome().getIdGenome());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SampleGenomeLinkUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (SampleGenomeLink) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SampleGenomeLinkDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public SampleGenomeLink getSampleGenomeLink(jpa.entities.SampleGenomeLinkPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = SampleGenomeLink.class)
    public static class SampleGenomeLinkControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SampleGenomeLinkController controller = (SampleGenomeLinkController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sampleGenomeLinkController");
            return controller.getSampleGenomeLink(getKey(value));
        }

        jpa.entities.SampleGenomeLinkPK getKey(String value) {
            jpa.entities.SampleGenomeLinkPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.SampleGenomeLinkPK();
            key.setIdSample(Integer.parseInt(values[0]));
            key.setIdGenome(values[1]);
            key.setType(values[2]);
            return key;
        }

        String getStringKey(jpa.entities.SampleGenomeLinkPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdSample());
            sb.append(SEPARATOR);
            sb.append(value.getIdGenome());
            sb.append(SEPARATOR);
            sb.append(value.getType());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SampleGenomeLink) {
                SampleGenomeLink o = (SampleGenomeLink) object;
                return getStringKey(o.getSampleGenomeLinkPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SampleGenomeLink.class.getName());
            }
        }

    }

    //Limpiar valores del formulario
    public void cleanFormSample() {
        haveCont = false;
        haveBioAn = false;

    }

}
