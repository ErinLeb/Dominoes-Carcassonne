package shared.model;

public class Player {
    protected int score = 0;
    protected String name;
    protected boolean isInGame = true;

    protected static final String[] botNames = { "Aiwendil", "Alatar", "Amroth", "Anarion", "Ancalagon", "Annatar",
            "Aragorn", "Arathorn", "Arien", "Arvedui", "Asfaloth", "Aule", "Beleg", "Beorn", "Beregond", "Beruthiel",
            "Bilbo", "Bill", "Boromir", "Carcaroth", "Celeborn", "Celebrimbor", "Cirdan", "Curumo", "Deagol",
            "Denethor", "Dior", "Durin", "Earendil", "Ecthelion", "Elanor", "Elbereth", "Elendil", "Elladan", "Elrohir",
            "Elrond", "Elros", "Elwe", "Elwing", "Engwe", "Eol", "Eomer", "Eonwe", "Eorl", "Eowyn", "Este", "Fangorn",
            "Faramir", "Feanaro", "Feanor", "Filgolfin", "Finarfin", "Finguilas", "Finrod", "Frodo", "Galadriel",
            "Gandalf", "Gil-Galad", "Gimli", "Glaurung", "Gloin", "Glorfindel", "Goldberry", "Gollum", "Gothmog",
            "Grima", "Gwaihir", "Hama", "Helm", "Huan", "Hurin", "Idril", "Ilmare", "Indis", "Irmo", "Isildur",
            "Khamul", "Legolas", "Lorien", "Luthien", "Maedhros", "Mairon", "Mandos", "Manwe", "Melian", "Melkor",
            "Meriadoc", "Min", "Morgoth", "Nessa", "Nienna", "Niennor", "Ohtar", "Olorin", "Orodreth", "Orome", "Osse",
            "Pallando", "Peregrin", "Quickbeam", "Radagast", "Rose", "Salmar", "Sam", "Saruman", "Sauron", "Scatha",
            "Shadowfax", "Shelob", "Smaug", "Smeagol", "Tar-Minyatur", "Tar-Palatir", "Thengel", "Theoden", "Thingol",
            "Thorin", "Thorondor", "Thrain", "Tilion", "Tom Bombadil", "Treebeard", "Tulkas", "Tuor", "Turgon", "Turin",
            "Uinen", "Ulmo", "Ungoliant", "Vaire", "Vana", "Varda", "Yavanna" };

    // Constructor
    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    // Getters

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public boolean isInGame() {
        return isInGame;
    }

    // Setters

    public void setInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Methods

    /**
     * Resets the score to 0.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Add {@code points} to the score of the player
     * 
     * @param points
     */
    public void incrementScore(int points) {
        this.score += points;
    }

    /**
     * Returns {@code true} if the score of the player is equal to p's score
     * 
     * @param p
     * @return {@code true} if the score of the player is equal to p's score
     */
    public boolean isScoreEqual(Player p) {
        return score == p.score;
    }

}