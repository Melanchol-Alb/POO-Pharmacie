import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Date;

public class Donnees {

public static MedicamentEnInterne RecupMedicInter(String ligne) {
	//  nom|type|mode|0|[ing1-q1;ing2-q2...;]
	ArrayList<String> str = new ArrayList<String>();//recupere nom type mode et ordonnance
	ArrayList<Paire<String,Double>> ingreds= new ArrayList<Paire<String,Double>>();
	int i=0,j=0;
	while(i<4) {
		String s="";
		while(ligne.charAt(j)!='|') {
			s=s+ligne.charAt(j);
			j++;
		}
		j++;
		str.add(s);
		i++;
	}
	j=j+1; //sauter vers après le [
	while(j<ligne.length()-2) { //ne pas atteindre le ]
		String s1=""; //recuper le nom de l'ingred
		while(ligne.charAt(j)!='-') {
			s1=s1+ligne.charAt(j);
			j++;
		}
		j++;
		String s2=""; //recupere la doze
		while(ligne.charAt(j)!=';') {
			s2=s2+ligne.charAt(j);
			j++;
		}
		j++;
		ingreds.add(new Paire<String,Double>(s1,Double.parseDouble(s2)));
	}
	return (new MedicamentEnInterne(str.get(0),str.get(1),str.get(2),Boolean.parseBoolean(str.get(3)),ingreds));
}

public static MedicamentProduitEnExterne RecupMedicExter(String ligne) throws IOException {
	ArrayList<String> str = new ArrayList<String>();//recupere nom type et mode t ordonnance et fournisseur
	int i=0,j=0;
	while(i<4) {
		String s="";
		while((ligne.charAt(j)!='|')&&(j<ligne.length()-1)) {
			s=s+ligne.charAt(j);
			j++;
		}
		j++;
		str.add(s);
		i++;
	}
	ArrayList<Fournisseur> Fournisseurs= new ArrayList<Fournisseur>(); 
	while(j!=ligne.length()) {
		String s="";
		while((ligne.charAt(j)!='|')&&(j<ligne.length()-1)) {
			s=s+ligne.charAt(j);
			j++;
		}
		j++;
		Fournisseurs.add(new Fournisseur(s));
	}
	return (new MedicamentProduitEnExterne(str.get(0),str.get(1),str.get(2),Boolean.parseBoolean(str.get(3)),Fournisseurs));
}

public static String coderMedic(Medicament med) {
	String s=med.nom+"|"+med.type+"|"+med.mode+"|"+med.ordonnance+"|";
	if(med instanceof MedicamentEnInterne) {
		Iterator<Paire<String,Double>> i =((MedicamentEnInterne)med).ingredients.iterator();
		Paire<String,Double> p;
		s=s+"[";
		while(i.hasNext()) {
			p=i.next();
			s=s+p.valeur+"-"+p.valeur2+";";
		}
		s=s+"]|";
	}
	if(med instanceof MedicamentProduitEnExterne) {
		for(int i=0;i<((MedicamentProduitEnExterne)med).getFournisseur().size();i++)
		s=s+((MedicamentProduitEnExterne)med).getFournisseur().get(i).getNom()+"|";
	}
	return s;
}

public static HashMap<String,MedicamentEnInterne> RecupererMedicInter() throws IOException {
	HashMap<String,MedicamentEnInterne> med = new HashMap<String,MedicamentEnInterne>();
	File Interne= new File("Interne.stk");
	BufferedReader in = new BufferedReader(new FileReader(Interne));
	String str;
	while ((str=in.readLine()) != null) {
		MedicamentEnInterne m = RecupMedicInter(str);
		med.put(m.nom, m);
	}
	in.close();
	return med;
}

public static HashMap<String,MedicamentProduitEnExterne> RecupererMedicExter() throws IOException {
	HashMap<String,MedicamentProduitEnExterne> med = new HashMap<String,MedicamentProduitEnExterne>();
	File Externe= new File("Externe.stk");
	BufferedReader in = new BufferedReader(new FileReader(Externe));
	String str;
	while ((str=in.readLine()) != null) {
		MedicamentProduitEnExterne m = RecupMedicExter(str);
		med.put(m.nom, m);
	}
	in.close();
	return med;
}

public static void EnregistrerMedicaments(boolean interne,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) throws IOException {
	if(interne) {
		File Interne= new File("Interne.stk");
		FileWriter outin = new FileWriter(Interne);
		for(String i: inter.keySet()) {
			outin.write(Donnees.coderMedic(inter.get(i))+"\r\n");
		}
		outin.close();
	}
	else {
		File Externe= new File("Externe.stk");
		FileWriter outex = new FileWriter(Externe);
		for(String i: exter.keySet()) {
			outex.write(Donnees.coderMedic(exter.get(i))+"\r\n");
		}
		outex.close();
	}
}

public static void EnregistrerStock(HashMap<String, ArrayList<MedicamentEnStock>> Stock, HashMap<String,MedicamentProduitEnExterne> exter, HashMap<String,MedicamentEnInterne> inter) throws IOException {
	File stockInterne = new File("StockInterne.stk"),stockExterne = new File("StockExterne.stk");
	FileWriter outin = new FileWriter(stockInterne);
	FileWriter outex = new FileWriter(stockExterne);
	for (String i : Stock.keySet()) {
		  String in="",ex="";
		  for(int j=0;j<Stock.get(i).size();j++) {
			  if(Stock.get(i).get(j).getMedicament() instanceof MedicamentProduitEnExterne)
				  ex=ex+Stock.get(i).get(j).getQuantité()+","+Stock.get(i).get(j).getNumeroLot()+","+Stock.get(i).get(j).getDateExpiration().getTime()+","+Stock.get(i).get(j).getPrix()+"-";
			  if(Stock.get(i).get(j).getMedicament() instanceof MedicamentEnInterne)
				  in=in+Stock.get(i).get(j).getQuantité()+","+Stock.get(i).get(j).getNumeroLot()+","+Stock.get(i).get(j).getDateExpiration().getTime()+","+Stock.get(i).get(j).getPrix()+"-";
		  }
		  if(!ex.equals(""))
			  outex.write(i+"-"+ex+"\r\n");
		  if(!in.equals(""))
			  outin.write(i+"-"+in+"\r\n");
		  if((in.equals(""))&&(ex.equals(""))) {
			  if(exter.get(i)!=null)
			  outex.write(i+"-\r\n");
			  if(inter.get(i)!=null)
			  outin.write(i+"-\r\n");
		  }
		}
	outin.close();
	outex.close();
}

public static HashMap<String, ArrayList<MedicamentEnStock>> RecupererStock(HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) throws IOException {
	//structure fichier stock = nom medoc-quantité,numerolot,date(long),prix-quantité,numerolot,date(long),prix...
	// deux fichiers, stock interne et stock externe
	HashMap<String, ArrayList<MedicamentEnStock>> Stock=new HashMap<String, ArrayList<MedicamentEnStock>>() ;
	File Externe= new File("StockExterne.stk");
	File Interne= new File("StockInterne.stk");
	BufferedReader in = new BufferedReader(new FileReader(Externe));
	String str;
	while ((str=in.readLine()) != null) {
		int j=0;
		String nom="";
		while(str.charAt(j)!='-') {
			nom=nom+str.charAt(j);
			j++;
		}
		j++;//se positionner sur après le -
		if(j+1!=str.length())
		if(exter.get(nom)==null)
			System.out.println(nom+" non present sur le fichier des medicaments produits en externe, médicament ignoré");
		else {
		ArrayList<MedicamentEnStock> Meds = new ArrayList<MedicamentEnStock>();
		while(j!=str.length()) {
			String quan="";
			while(str.charAt(j)!=',') {
				quan=quan+str.charAt(j);
				j++;
			}
			j++;
			String lot="";
			while(str.charAt(j)!=',') {
				lot=lot+str.charAt(j);
				j++;
			}
			j++;
			String date="";
			while(str.charAt(j)!=',') {
				date=date+str.charAt(j);
				j++;
			}
			j++;
			String prix="";
			while(str.charAt(j)!='-') {
				prix=prix+str.charAt(j);
				j++;
			}
			j++;
			Meds.add(new MedicamentEnStock(exter.get(nom),Integer.parseInt(quan),Integer.parseInt(lot),new Date(Long.parseLong(date)),Double.parseDouble(prix)));
		}
		Stock.put(nom, Meds);
	}
		else {//si on a une ligne nomMed- sans aucun lot
			if(exter.get(nom)==null)
				System.out.println(nom+" non present sur le fichier des medicaments produits en externe, médicament ignoré");
			else {
			ArrayList<MedicamentEnStock> Meds = new ArrayList<MedicamentEnStock>();
			Stock.put(nom, Meds);
			}
		}
	}
	in.close();
	BufferedReader in2 = new BufferedReader(new FileReader(Interne));
	while ((str=in2.readLine()) != null) {
		int j=0;
		String nom="";
		while(str.charAt(j)!='-') {
			nom=nom+str.charAt(j);
			j++;
		}
		j++;//se positionner sur après le -
		if(j+1!=str.length())
		if(inter.get(nom)==null)
			System.out.println(nom+" non present sur le fichier des medicaments en interne, médicament ignoré");
		else {
		ArrayList<MedicamentEnStock> Meds = new ArrayList<MedicamentEnStock>();
		while(j!=str.length()) {
			String quan="";
			while(str.charAt(j)!=',') {
				quan=quan+str.charAt(j);
				j++;
			}
			j++;
			String lot="";
			while(str.charAt(j)!=',') {
				lot=lot+str.charAt(j);
				j++;
			}
			j++;
			String date="";
			while(str.charAt(j)!=',') {
				date=date+str.charAt(j);
				j++;
			}
			j++;
			String prix="";
			while(str.charAt(j)!='-') {
				prix=prix+str.charAt(j);
				j++;
			}
			j++;
			Meds.add(new MedicamentEnStock(inter.get(nom),Integer.parseInt(quan),Integer.parseInt(lot),new Date(Long.parseLong(date)),Double.parseDouble(prix)));
		}
		if(Stock.get(nom)!=null){
			Meds.addAll(Stock.get(nom));
			Stock.remove(nom);
		}
		Stock.put(nom, Meds);
		}
		else {//si on a une ligne nomMed- sans aucun lot
			if(exter.get(nom)==null)
				System.out.println(nom+" non present sur le fichier des medicaments en interne, médicament ignoré");
			else {
			ArrayList<MedicamentEnStock> Meds = new ArrayList<MedicamentEnStock>();
			Stock.put(nom, Meds);
			}
		}
	}
	in2.close();
	return Stock;
}

public static void AjoutStockInterne(MedicamentEnStock med,HashMap<String,MedicamentEnInterne> meds,HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter) throws IOException {
	if(meds.get(med.getMedicament().nom)==null)
		meds.put(med.getMedicament().getNom(), (MedicamentEnInterne)med.getMedicament());
	if(Stock.get(med.getMedicament().getNom())==null) {
		ArrayList<MedicamentEnStock> A = new ArrayList<MedicamentEnStock>();
		A.add(med);
		Stock.put(med.getMedicament().getNom(), A);
	}
	else Stock.get(med.getMedicament().getNom()).add(med);
	Donnees.EnregistrerStock(Stock, exter, meds);
	EnregistrerMedicaments(true,null,meds);
}

public static void NettoyerStock(HashMap<String, ArrayList<MedicamentEnStock>> Stock, HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter)throws IOException{
	for (String i : Stock.keySet()) {
		int n=0;
		for(int j=Stock.get(i).size()-1;j>=0;j--) {
			if((Stock.get(i).get(j).getQuantité()==0)||(Stock.get(i).get(j).getDateExpiration().getTime()<Donnees.Aujourdhui()))
				Stock.get(i).remove(j);
			else n+=Stock.get(i).get(j).getQuantité();
		}
		if(n<10)
			System.out.println("Songez a vous réapprovisionner en "+i+". "+n+" pièces restantes.");
	}
	EnregistrerStock(Stock,exter,inter);
}

public static void Acheter(boolean ord,ArrayList<MedicamentPrescrit> prescription, HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter, JFrame f){//The String key to the hash map is Medicament.nom+;;
	try {	
		long date = Aujourdhui();
		Double payer=0.;
		ArrayList <MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
		ArrayList<Integer> remove = new ArrayList<Integer>();
		int r;
		for(int i=0;i<prescription.size();i++) {
			MedicamentPrescrit p = prescription.get(i);
			ArrayList<MedicamentEnStock> Meds = Stock.get(p.nom);
			boolean skip = false;
			if(Meds==null) {
				r = JOptionPane.showConfirmDialog(f, p.nom+" indisponible, Souhaitez-vous continuer la transaction?", "Continuer",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(r==JOptionPane.NO_OPTION)
					return;
				skip= true;
				remove.add(i);
			}//PopUpDurantAchat(, pr, p, this);
			if(!skip) {
				if((skip==false)&&(!ord)&&(!Stock.get(p.nom).get(0).getMedicament().isOrdonnance())) {
					r = JOptionPane.showConfirmDialog(f, "Le medicament "+p.nom+" n'est pas disponible sans ordonnance. Souhaitez-vous continuer la transaction?", "Continuer",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(r==JOptionPane.NO_OPTION)
						return;
					skip= true;
					remove.add(i);
				}
				Iterator<MedicamentEnStock> j = Meds.iterator();
				boolean t = false;
				while((!t)&&(j.hasNext())) {
					if(j.next().getQuantité()>=p.quantité)
						t=true;
				}
				if(!t) {
					r = JOptionPane.showConfirmDialog(f, p.nom+" indisponible, Souhaitez-vous continuer la transaction?", "Continuer?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(r==JOptionPane.NO_OPTION)
						return;
					skip= true;
					remove.add(i);
				}
				if(skip==false) {
					j = Meds.iterator();
					t = false;
					while((!t)&&(j.hasNext())) {
						if(j.next().getDateExpiration().getTime()>=p.durée*86400000+date)
							t=true;
					}
					if(!t) {
						r = JOptionPane.showConfirmDialog(f, p.nom+" indisponible, Souhaitez-vous continuer la transaction?", "Continuer?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(r==JOptionPane.NO_OPTION)
							return;
						skip= true;
						remove.add(i);
					}
				}
			}
		}
		
		for(int i=remove.size()-1;0<=i;i--) 
			prescription.remove((int)remove.get(i));
			
		for(int i=0;i<prescription.size();i++) {
			MedicamentPrescrit p = prescription.get(i);
			ArrayList<MedicamentEnStock> Meds = Stock.get(p.nom);
			int j=0;
			while(j<Meds.size()) {
				if(Meds.get(j).getQuantité()>=p.quantité) {
					Meds.set(j, new MedicamentEnStock(Meds.get(j).getMedicament(), Meds.get(j).getQuantité()-p.quantité, Meds.get(j).getNumeroLot(), Meds.get(j).getDateExpiration(), Meds.get(j).getPrix()));
					payer+=Meds.get(j).getPrix()-Meds.get(j).TauxDeRemboursement();
					presc.add(new MedicamentPrescrit(p.nom, p.quantité, Meds.get(j).getNumeroLot()));
					j= Meds.size();
				}
				j++;
			}
		}
		Main.TransactionSucces(payer, presc);
		Donnees.NettoyerStock(Stock,exter,inter); //Enregistrer stock se fait implicitement
		}
		catch(IOException e) {
		}
	}

public static long Aujourdhui() {
	Date d= new Date();
	return d.getTime();
}

public static String[] getTypes() {
	String[] Types = new String[24];
	Types[0] = "Anesthésiant";
	Types[1] = "Analgésique";
	Types[2] = "Antibiotique";
	Types[3] = "Antidepresseur";
	Types[4] = "Antidiurétique";
	Types[5] = "Anti-inflammatoire";
	Types[6] = "Antihistaminique";
	Types[7] = "Antihypertenseur";
	Types[8] = "Antipyrétique";
	Types[9] = "Antiviral";
	Types[10] = "Antirétroviral";
	Types[11] = "Antitussif";
	Types[12] = "Anxiolytique";
	Types[13] = "Brochodilatateur";
	Types[14] = "Diurétique";
	Types[15] = "Hypnotique";
	Types[16] = "Laxatif";
	Types[17] = "Neuroleptique";
	Types[18] = "Psychotrope";
	Types[19] = "Sédatif";
	Types[20] = "Thymorégulateur";
	Types[21] = "Vasopresseur";
	Types[22] = "Autre";
	Types[23] = "Parapharmaceutique";
	return Types;
}

public static String[] getModes() {
	String[] Modes = new String[15];
	Modes[0] = "Buccal";
	Modes[1] = "Intra-artérielle";
	Modes[2] = "Intra-articulaire";
	Modes[3] = "Intracardiaque";
	Modes[4] = "Intradermique";
	Modes[5] = "Intramusculaire";
	Modes[6] = "Intraosseux";
	Modes[7] = "Intrathécale";
	Modes[8] = "Intraveineux";
	Modes[9] = "Nasal";
	Modes[10] = "Oral";
	Modes[11] = "Rectal";
	Modes[12] = "Sous-cutané";
	Modes[13] = "Vaginal";
	Modes[14] = "Autre";
	return Modes;
}
}
