module alissouali.amdalatjinadu.tp5_morpion {
    requires javafx.controls;
    requires javafx.fxml;

    opens alissouali.amdalatjinadu.morpion to javafx.fxml;
   // exports alissouali.amdalatjinadu.morpion;
    exports alissouali.amdalatjinadu.morpion.vue;
    opens alissouali.amdalatjinadu.morpion.vue to javafx.fxml;
    exports alissouali.amdalatjinadu.morpion.service;
    opens alissouali.amdalatjinadu.morpion.service to javafx.fxml;




   /* opens alissouali.amdalatjinadu.morpion to javafx.fxml;
    exports alissouali.amdalatjinadu.morpion;

    exports alissouali.amdalatjinadu.morpion.vue;
    opens alissouali.amdalatjinadu.morpion.vue to javafx.fxml;

    exports alissouali.amdalatjinadu.morpion.service;
    opens alissouali.amdalatjinadu.morpion.service to javafx.fxml;*/
}