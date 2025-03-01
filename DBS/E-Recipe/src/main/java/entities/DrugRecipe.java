package entities;

import jakarta.persistence.*;


@Entity
@Table(name = "drug_recipe")
public class DrugRecipe {
    @EmbeddedId
    private DrugRecipeId id;

    @MapsId("drugId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @MapsId("recipeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "number", nullable = false)
    private Integer number;

    public DrugRecipeId getId() {
        return id;
    }

    public void setId(DrugRecipeId id) {
        this.id = id;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}