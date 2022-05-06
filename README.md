# DrunkShopApp

Italos alkalmazás

## Help for pontozás

1, Fordítási hiba nincs:
- nincs

2, Futtatási hiba nincs:
- nincs

3, Firebase autentikáció meg van valósítva:
- be lehet lépni
- be lehet regisztrálni

4, Adatmodell definiálása (class vagy interfész formájában):
- italok modell -> Drink.java

5, Legalább 3 különböző activity használata:
- DashboardActivity
- MainActivity
- RegActivity

6, Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.):
- jó billentyűzet van mindenhol is

7, ConstraintLayout és még egy másik layout típus használata:
- Constraint
- Relative
- Lineal

8, Reszponzív:
- többnyire :D

9, Legalább 2 különböző animáció használata:
- anim/slide_row.xml -> beúsznak az elemek a felsorolásnál
- anim/spin.xml -> a képek forognak a "SPIN ME" gomb hatására

10, Intentek használata: navigáció meg van valósítva az activityk között (minden activity elérhető):
- minden activity elérhető ;)

11, Legalább egy Lifecycle Hook használata a teljes projektben: (kiv onCreate és logolás):
- onDestroy -> DashboardActivity
- onPause -> DashboardActivity, MainActivity
- onResume -> DashboardActivity

12, Legalább egy olyan androidos erőforrás használata, amihez kell permission:
- nincs

13, Legalább egy notification vagy alam manager vagy job scheduler használata:
- notification üzenetek kosárba tételnél -> NotifHandler és DashboardActivityben van használva

14, CRUD műveletek mindegyike megvalósult és AsyncTask:
- create, delete, read -> Drink
- AsyncTask -> AsyncTaskClass + AsyncLoader, megváltozik a MainActivityben a gomb felairata bizonyos időközönként

15, Legalább 2 komplex Firestore lekérdezés megvalósítása:
- limit -> DashboardActivity
- orderBy -> DashboardActivity

16: Szubjektív pontozás:
- “¯\_(ツ)_/¯“
