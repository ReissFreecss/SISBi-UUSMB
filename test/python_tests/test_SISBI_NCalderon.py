from playwright.sync_api import Playwright, sync_playwright, expect
from .common import login, escoge_proyecto

def hace_analisis(page, variante):
    login(page)
    page.get_by_placeholder("ID o nombre del proyecto").fill("mejia")
    page.get_by_role("button", name="Buscar").click()
    page.get_by_role("gridcell", name="Project_NCalderon_2020_08_05_07_29_45").click()
    page.get_by_role("button", name="Registrar reporte(s) bioinformático(s)").click()
    page.locator("[id=\"form\\:An_label\"]").click()
    page.get_by_role("button", name="Generar Reporte").click()
    page.get_by_role("button", name=variante, exact=True).click()
    page.get_by_role("button", name="Generar Word").click()

    with page.expect_download() as download_info:
        page.get_by_role("button", name="Descargar Reporte Actualizado").click()
    download = download_info.value

    pass

def run(playwright: Playwright) -> None:
    browser = playwright.firefox.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()
    login(page)
    escoge_proyecto(page, "Project_NCalderon_2020_08_05_07_29_45")
    page.get_by_role("button", name="Generar Reporte").click()
    page.get_by_role("button", name="Expresión Diferencial", exact=True).click()
    page.get_by_role("button", name="Generar Word").click()
    with page.expect_download() as download_info:
        page.get_by_role("button", name="Descargar Reporte Actualizado").click()
    download = download_info.value
    download.save_as(download.suggested_filename)

    # ---------------------
    context.close()
    browser.close()

def run0(playwright: Playwright) -> None:
    browser = playwright.firefox.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()
    login(page)
    variantes = ("Análisis de expresión diferencial", "Resecuenciación de genomas", "Variantes Genéticas",
                 "Metagenomas", "Ensamblado de novo", "Análisis de expresion diferencial y Ensamblado de transcriptoma")
    #for variante in variantes:
    #    hace_analisis(page, variante)
    hace_analisis(page, "Expresión Diferencial")
    page.close()

    # ---------------------
    context.close()
    browser.close()


with sync_playwright() as playwright:
    run(playwright)
