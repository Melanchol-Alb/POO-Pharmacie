import java.util.Date;

public class MedicamentEnStock {
	private Medicament medicament;
	private int quantité,numeroLot;
	private Date dateExpiration;
	private double prix;
	public MedicamentEnStock(Medicament medicament, int quantité, int numeroLot, Date dateExpiration, double prix) {
		this.medicament = medicament;
		this.quantité = quantité;
		this.numeroLot = numeroLot;
		this.dateExpiration = dateExpiration;
		this.prix = prix;
	}
	public double TauxDeRemboursement() {
		if(medicament.type!="Parapharmaceutique") {
			if(medicament instanceof MedicamentProduitEnExterne)
				return prix*10/100;
			if(medicament instanceof MedicamentEnInterne)
				return prix*20/100;
		}
		return 0;
	}
	public int getQuantité() {
		return quantité;
	}
	public void setQuantité(int quantité) {
		this.quantité = quantité;
	}
	public Date getDateExpiration() {
		return dateExpiration;
	}
	public Medicament getMedicament() {
		return medicament;
	}
	public int getNumeroLot() {
		return numeroLot;
	}
	public double getPrix() {
		return prix;
	}
	@Override
	public String toString() {
		return "MedicamentEnStock [medicament=" + medicament + ", quantité=" + quantité + ", numeroLot=" + numeroLot
				+ ", dateExpiration=" + dateExpiration + ", prix=" + prix + "]";
	}
}
