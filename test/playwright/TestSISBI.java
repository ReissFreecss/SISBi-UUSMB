package playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.*;

public class TestSISBI {
  public static void main(String[] args) {
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(false));
      BrowserContext context = browser.newContext();
      Page page = context.newPage();
      page.navigate("http://local.uusmb.unam.mx:8080/SISBI/");
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Iniciar sesión")).click();
      page.getByLabel("Usuario o correo electrónico:*").click();
      page.getByLabel("Usuario o correo electrónico:*").fill("LMontero");
      page.getByLabel("Contraseña*").click();
      page.getByLabel("Contraseña*").fill("NVT$Yj4nJk%B$T3YnSa@");
      page.getByLabel("Iniciar sesión").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Iniciar sesión")).click();
      page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName("Alejandro").setExact(true)).click();
      page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName("Project_AAlagon_2019-11-20_10:48:30")).click();
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Registrar reporte(s) bioinformático(s)")).click();
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Calcular")).click();
    }
  }
}