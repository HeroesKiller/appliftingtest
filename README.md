# Applifting - Java BE developer exercise

## O projektu
**Výstupem projektu je REST API JSON Java microservice, která umožňuje monitorovat URL, konkrétně:**
 - CRUD operace pro URL a výpis pro daného uživatele
 - monitorování URL na pozadí, logování status code a payload
 - pro každou URL zobrazení 10 posledních výsledků monitoringu

**Pár implementačních poznámek:**
- **Autentizaci/autorizaci** jsem dle zadání prováděl přes accessToken v headeru, běžně bych zabezpečení řešil spíše přihlašováním a rolemi s pomocí Spring Security, jelikož accessToken je již u jednotlivých uživatelů v databázi.
- Co se **Dockeru** týče, omlouvám se, pokud nebude fungovat korektně. V tuto chvíli jsem u velice starého PC s Win 10 Home, který Docker oficiálně nepodporuje a jeho instalace a spouštění se musí řešit přes virtualbox (což tento počítač těžko rozdýchává) a nastavování proměnných s každou instancí.  Nabízím tedy dvě alternativy spuštění aplikace.


## Jak spustit aplikaci
### Metoda 1. (Docker)

 1. Otevřete konzoli/terminál v hlavním adresáři (appliftingtest)
 2. Vytvořte docker image: `docker build -f Dockerfile -t applmonitoring .`
 3. Spusťte docker image: `docker run -p 8080:8080 applmonitoring`

### Metoda 2.
V tomto případě je nutno mít staženou Javu, a to alespoň verzi 8.

 1. Otevřete konzoli/terminál v adresáři appliftingtest/target
 2. Spusťte aplikaci pomocí příkazu: `java -jar applmonitoring.jar`

## Jak používat aplikaci 
Po spuštění bude aplikace poslouchat na portu 8080 localhostu (např. http://localhost:8080/endpoints).
Controller podporuje následující operace:
|metoda|endpoint|požadavek*|info
|--|--|--|--|
| GET | /endpoints  |  | seznam všech monitorovaných endpointů uživatele
| GET | /endpoints/{id}  | id endpointu [Long] | vrátí info o konkrétním endpointu
| POST| /endpoints  | MonitoredEndpoint (body) | vytvoří endpoint s danými parametry
| PUT| /endpoints/{id}  | id endpointu, MonitoredEndpoint (body)| aktualizuje daný endpoint
| DELETE | /endpoints/{id}  | id endpointu  | odstraní endpoint z databáze
| GET | /endpoints/{id}/results  | id endpointu | posledních 10 logů daného endpointu

Příklad validního JSON body MonitoredEndpointu je následující (jméno je nepovinné):
```json
{
	"name": "Google",
	"url": "https://www.google.cz/",
	"monitoredInterval": 3
}
```

Testováno např. na:
- https://www.applifting.cz/
- https://www.google.com/
- https://twitter.com/
