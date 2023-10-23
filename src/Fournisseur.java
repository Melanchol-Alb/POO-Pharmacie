import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Fournisseur {
	private String nom,numero,email;
	
	@Override
	public String toString() {return "Fournisseur nommé " + nom + ", son numero de téléphone " + numero + ", son adresse mail " + email + "]";}

	public Fournisseur(String nom) throws IOException {
		this.nom = nom;
		File fournisseur = new File("Fournisseurs.stk");
		BufferedReader in = new BufferedReader(new FileReader(fournisseur));
		String str;
		boolean notFound=true;
		while(((str=in.readLine())!=null)&&(notFound)) {
			int j=0;
			String name="";
			while(str.charAt(j)!=',') {
				name+=str.charAt(j);
				j++;
			}
			j++;
			if(name.equals(nom)) {
				notFound=false;
				String num="";
				while(str.charAt(j)!=',') {
					num+=str.charAt(j);
					j++;
				}
				j++;
				this.numero=num;
				String mail="";
				while(str.charAt(j)!='-') {
					mail+=str.charAt(j);
					j++;
				}
				this.email=mail;
			}
		}
		if(notFound)
			System.out.println("Le fournisseur "+nom+" n'est pas enregistré !");
		in.close();
	}
	
	public Fournisseur(String nom,String num,String mail) throws IOException {
		this.nom=nom;
		this.numero=num;
		this.email=mail;
		if(!existe(nom)){
		File fournis = new File("Fournisseurs.stk");
		FileWriter out = new FileWriter(fournis,true);
		out.write(nom+","+num+","+mail+"-\r\n");
		out.close();
		}
	}
	
	public static boolean existe(String Nom) throws IOException {
		File fournis = new File("Fournisseurs.stk");
		BufferedReader in = new BufferedReader(new FileReader(fournis));
		String str;
		while((str=in.readLine())!=null) {
			int j=0;
			String nom="";
			while(str.charAt(j)!=',') {
				nom+=str.charAt(j);
				j++;
			}
			if(nom.equals(Nom)) {
				in.close();
				return true;
			}
		}
		in.close();
		return false;
	}
	
	public String getNom() {return nom;}

	public void fournis(MedicamentEnStock med,HashMap<String,MedicamentProduitEnExterne> meds,HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentEnInterne> inter) throws IOException {
		if(meds.get(med.getMedicament().nom)==null)
			meds.put(med.getMedicament().getNom(), (MedicamentProduitEnExterne)med.getMedicament());
		if(Stock.get(med.getMedicament().getNom())==null) {
			ArrayList<MedicamentEnStock> A = new ArrayList<MedicamentEnStock>();
			A.add(med);
			Stock.put(med.getMedicament().getNom(), A);
		}
		else Stock.get(med.getMedicament().getNom()).add(med);
		int i=0;
		while((i<meds.get(med.getMedicament().getNom()).getFournisseur().size())&&(!meds.get(med.getMedicament().getNom()).getFournisseur().get(i).getNom().equals(nom)))
			i++;
		if(i==meds.get(med.getMedicament().getNom()).getFournisseur().size())
			meds.get(med.getMedicament().getNom()).getFournisseur().add(this);
		Donnees.EnregistrerStock(Stock,meds,inter);
		Donnees.EnregistrerMedicaments(false, meds, null);
	}
}
