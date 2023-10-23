import java.util.ArrayList;

public class MedicamentProduitEnExterne extends Medicament {
	private ArrayList<Fournisseur> fournisseur;
	@Override
	public String toString() {
		return "MedicamentProduitEnExterne [fournisseur=" + fournisseur + ", nom=" + nom + ", type=" + type + ", mode="
				+ mode + ", ordonnance=" + ordonnance + "]";
	}
	
	public ArrayList<Fournisseur> getFournisseur() {return fournisseur;}
	
	public MedicamentProduitEnExterne(String nom, String type, String mode, boolean ordonnance, ArrayList<Fournisseur> fr) {
		super(nom, type, mode, ordonnance);
		fournisseur = fr;
	}
}
