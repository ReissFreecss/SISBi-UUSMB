from playwright.sync_api import Playwright, sync_playwright, expect
from .common import login

def run(playwright: Playwright) -> None:
    browser = playwright.chromium.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()
    page.goto("http://local.uusmb.unam.mx:8080/SISBI/")
    login(page)
    page.get_by_role("gridcell", name="AAlagon").click()
    page.get_by_role("gridcell", name="Project_AAlagon_2019-11-20_10:48:30").click()
    page.get_by_role("button", name="Registrar reporte(s) bioinformático(s)").click()
    page.get_by_role("button", name="Calcular").click()

    # ---------------------
    context.close()
    browser.close()


with sync_playwright() as playwright:
    run(playwright)