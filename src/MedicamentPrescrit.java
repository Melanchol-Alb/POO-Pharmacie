public class MedicamentPrescrit {
	protected String nom;
	protected int quantité; 
	protected int durée;//en jours
	public MedicamentPrescrit(String nom, int quantité, int durée) {
		this.nom = nom;
		this.quantité = quantité;
		this.durée = durée;}
	@Override
	public String toString() {
		return "MedicamentPrescrit [nom=" + nom + ", quantité=" + quantité + ", durée=" + durée + "]";}
	
}
