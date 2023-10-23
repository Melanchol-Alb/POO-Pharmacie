import java.util.ArrayList;
public class MedicamentEnInterne extends Medicament{
	protected ArrayList<Paire<String, Double>> ingredients;
	@Override
	public String toString() {
		return "MedicamentEnInterne [ingredients=" + ingredients + ", nom=" + nom + ", type=" + type + ", mode=" + mode
				+ ", ordonnance=" + ordonnance + "]";
	}
	public MedicamentEnInterne(String nom, String type, String mode, boolean ordonnance, ArrayList<Paire<String, Double>> ingredients) {
		super(nom, type, mode, ordonnance);
		this.ingredients=null;
		double j=0;
		for(int i=0;i<ingredients.size();i++)
			j=j+ingredients.get(i).valeur2;
		if(j!=100)
			System.out.println("Il y a une erreur dans les dosages");
		else {
			this.ingredients = ingredients;
		}
	}
	
}
