public class Medicament {
	protected String nom;
	protected String type;
	protected String mode;
	protected boolean ordonnance;
	public Medicament(String nom, String type, String mode, boolean ordonnance) {
		this.nom = nom;
		this.type = type;
		this.mode = mode;
		this.ordonnance = ordonnance;
	}
	public String getType() {
		return type;
	}
	public String getNom() {
		return nom;
	}
	public String getMode() {
		return mode;
	}
	public boolean isOrdonnance() {
		return ordonnance;
	}
	
	
}
