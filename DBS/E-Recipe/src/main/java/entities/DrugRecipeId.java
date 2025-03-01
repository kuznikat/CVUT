package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Embeddable
public class DrugRecipeId implements java.io.Serializable {
    private static final long serialVersionUID = 2140959768018240872L;

    @Column(name = "drug_id", nullable = false)
    private Integer drugId;

    @Column(name = "recipe_id", nullable = false)
    private Integer recipeId;

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DrugRecipeId entity = (DrugRecipeId) o;
        return Objects.equals(this.drugId, entity.drugId) &&
                Objects.equals(this.recipeId, entity.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugId, recipeId);
    }

}