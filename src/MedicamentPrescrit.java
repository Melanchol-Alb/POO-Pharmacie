public class MedicamentPrescrit {
	protected String nom;
	protected int quantit�; 
	protected int dur�e;//en jours
	public MedicamentPrescrit(String nom, int quantit�, int dur�e) {
		this.nom = nom;
		this.quantit� = quantit�;
		this.dur�e = dur�e;}
	@Override
	public String toString() {
		return "MedicamentPrescrit [nom=" + nom + ", quantit�=" + quantit� + ", dur�e=" + dur�e + "]";}
	
}
