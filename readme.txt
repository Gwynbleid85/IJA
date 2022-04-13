Nazev projektu: ija-app
Clenove tymu: Milos Hegr (xhegrm00), Jiri Mladek (xmlade01)

Informace o projektu:
    Pro preklad jsme vyuzili nastroj maven. Zdrojove soubory jsou tedy v slozce 'src' a soubory prekladu se generuji do slozky 'target'.
    Maven si potrebne knihovny stahne sam, neni je tedy potreba zvlast specifikovat v skriptu 'get-libs.sh'.`

Zpusob prekladu:
        mvn package
Nasledne se vygeneruje jar soubor do slozky dest. Do teto slozky se presuneme.
Jar spustime prikazem 'java -jar ija-app.jar'.