public class Paire<T,Y> {
	public T valeur;
	public Y valeur2;
	public Paire(T valeur, Y valeur2) {
		this.valeur = valeur;
		this.valeur2 = valeur2;
	}
	
	@Override
	public String toString() {
		return valeur+"," +valeur2+"-";
	}
	
}
