package com.example.vkcup.crosszero;

class Rating {
    private String name;
    private String wins;

    Rating(String name, String wins) {
        this.name = name;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public String getWins() {
        return "Кол-во побед : " + wins;
    }
}
