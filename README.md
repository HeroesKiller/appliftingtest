# Applifting - Java BE developer exercise

## O projektu
**Výstupem projektu je REST API JSON Java microservice, která umožňuje monitorovat URL, konkrétně:**
 - CRUD operace pro URL a výpis pro daného uživatele
 - monitorování URL na pozadí, logování status code a payload
 - pro každou URL zobrazení 10 posledních výsledků monitoringu

**Pár implementačních poznámek:**
- **Autentizaci/autorizaci** jsem dle zadání prováděl přes accessToken v headeru, běžně bych zabezpečení řešil spíše přihlašováním a rolemi s pomocí Spring Security, jelikož accessToken je již u jednotlivých uživatelů v databázi.
- Co se **Dockeru** týče, omlouvám se, pokud nebude fungovat korektně. V tuto chvíli jsem u velice starého PC s Win 10 Home, který Docker oficiálně nepodporuje a jeho instalace a spouštění se musí řešit přes virtualbox (což tento počítač těžko rozdýchává) a nastavování proměnných s každou instancí.  Nabízím tedy dvě alternativy spuštění aplikace.
