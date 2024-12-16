import os, pathlib
import common
raiz = pathlib.Path(os.path.join(os.getcwd(), __file__)).parent.parent.parent/"doc"/"PlantillasReportes"
tipo_muestra = ("", "A16", "DNA", "RNAB", "RNAE")
class TipoMetodologia:
    def __init__(self, tipo_metodologia, zf=None):
        self.tipo_metodologia = tipo_metodologia
        self.tipos_muestra = {tm: common.TipoMetodologiaYMuestra(tipo_metodologia, tm, zf) for tm in tipo_muestra}
    def estudia_variantes(self, zf):
        base = None
        for (itmuestra, tmuestra) in enumerate(tipo_muestra):
            if itmuestra==0:
                base = self.tipos_muestra[tmuestra]
            else:
                print(f"Comparando {self.tipo_metodologia}: {tmuestra} vs base")
                base.align_texts(self.tipos_muestra[tmuestra])

    def estudia_variantes_2(self, zf):
        base = None
        for (itmuestra, tmuestra) in enumerate(tipo_muestra):
            if itmuestra==0:
                pass
            elif itmuestra==1:
                base = self.tipos_muestra[tmuestra]
            else:
                print(f"Comparando {self.tipo_metodologia}: {tmuestra} vs base")
                base.align_texts(self.tipos_muestra[tmuestra])

class TiposMetodologia:
    tipo_metodologias = (
    "analisismetagenomico", "busquedavariantes", "ensamblegenoma", "expresiondiferencial", "transcriptomanovo")

    def __init__(self, zf=None):
        self.tipos_metodologia = {tm: TipoMetodologia(tm, zf) for tm in self.tipo_metodologias}

    def estudia_variantes(self, zf=None):
        for tm in self.tipos_metodologia.values():
            tm.estudia_variantes(zf)

    def estudia_variantes_2(self, zf=None):
        for tm in self.tipos_metodologia.values():
            tm.estudia_variantes_2(zf)

    def estudio_transversal(self, zf=None):
        bases = {}
        for (itm, (ktm, tm)) in enumerate(self.tipos_metodologia.items()):
            for (ikvar, (kvar, variante)) in enumerate(tm.tipos_muestra.items()):
                print(f"{tm.tipo_metodologia}: {kvar}")
                if itm == 0:
                    bases[kvar] = variante
                else:
                    bases[kvar].align_texts(variante)

"""
Pendientes:
Link Github de Gena
Repetir:
Nota: La representatividad de la muestra no es responsabilidad del laboratorio ya que no realizamos muestreo. Queda prohibida la reproducción parcial de este reporte.
en todos los (tipos de) reportes al final
¿Siempre se ha usado la versiòn 1.1.3 de PROKKA?
Para TnovoED:
Finalmente se hizo una anotación automatica usando trinotate (Archivo 3): 
 VS: /scratch/vjimenez/Project/Project_NCalderon_2020_08_05_07_29_45/trinity_output/TrinoteRun/trinotate_results/trinotate_annotation_report.xls
donde se obtuvieron 574,676 anotaciones  para un poco mas de 113,473 genes unicos.
Luego... ¿siempre son 3 grupos de muestras?
BOST vs CER es un resultado particular, no?
y otros resultados por el estilo
La liga IDEamex está hardcodeada
Transdcriptoma de novo:
(/scratch/vjimenez/Project/Project_NCalderon_2020_08_05_07_29_45/trinity_output/TrinoteRun/trinotate_results/trinotate_annotation_report.xls:FIELDMANUALw)
donde se obtuvieron (574,676:FIELDMANUALx) anotaciones  para un poco más de (113,473:FIELDMANUALy) genes unicos. 
Después por indicación de Alejandro y Karel(???),  se repetió el ensambaldo por separado para cada uno de los 3(???) grupos de muestras, asi que se generaron los siguientes ensambles:

Ensamblado	Número de Transcritos 
trinity_(BOST:FIELDMANUALz)/Trinity.fasta	(165345:FIELDMANUALZa)
trinity_(CER:FIELDMANUALZb)/Trinity.fasta	(61522:FIELDMANUALZc)
trinity_(ONC:FIELDMANUALZd)/Trinity.fasta	(33925:FIELDMANUALZe)

Se realizó la anotación para cada uno de estos ensamblados con programa trinotate v3.1.1, y se utilizó el programa Cd-hit v 4.8.1, con el comando  CD-hit-est-2d para comparar las secuencias de los transcritos de cada par de ensamblados.  Los resultados de tales cluster se encuentran en los archivos con terminación out.clstr, pero realmente entre las tres especies no fueron significativos.  Se realizó después vs el ensamblado total, para verificar que se hubieran obtenido los mismos ensamblados. 

ANÁLISIS BIOINFORMÁTICO DE EXPRESIÓN DIFERENCIAL

Se generaron las tablas de coberturas con el programa RSEM v1.3.1, ejecutado desde trinity, con las coberturas para todas las isoformas de todos los transcritos, generando los archivos RSEM.gene.counts.matrix, con las coberturas de solo los transcritos primarios. Estas coberturas se subieron al sitio http://www.uusmb.unam.mx/ideamex/ para realizar el análisis de expresión diferencial y se solicitaron las comparaciones:
(BOST:FIELDMANUALz) vs (CER:FIELDMANUALZb)
(CER:FIELDMANUALZb) vs (ONC:FIELDMANUALZd)
(BOST:FIELDMANUALz) vs (ONC:FIELDMANUALZd)
...
Los resultados se encuentran disponibles en la liga de IDEAmex: http://www.uusmb.unam.mx/ideamex/(ncalderon194014:FIELDMANUALZf) 
"""

tsmet = TiposMetodologia()
tsmet.estudio_transversal()
