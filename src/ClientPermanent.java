import java.util.Date;
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

public class ClientPermanent {
	private String nom, prenom;
	private int age;
	private long numeroDeSecuriteSociale;
	private boolean chronique;
	private HashMap<String, Integer> traitements; //String est le nom du medicament en tant que key et le Integer le nbr d'unités achetés au total
	public ClientPermanent(String nom, String prenom, int age, long numeroDeSecuriteSociale, boolean chronique,HashMap<String,Integer> traitements) {
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.numeroDeSecuriteSociale = numeroDeSecuriteSociale;
		this.chronique = chronique;
		this.traitements = traitements;
	}
	
	public HashMap<String, Integer> getTraitements() {
		return traitements;
	}

	public String getNomComp() {
		return nom+" "+prenom;
	}
	
	public static boolean Existe(long SS) {
		File Clients = new File("Clients.stk");
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(Clients));
			String str;
			while ((str=in.readLine()) != null) {
				int j=0;
				String s="";
				while(str.charAt(j)!=',') {
					s+=str.charAt(j);
					j++;
				}
				if(Long.parseLong(s)==SS) {
					in.close();
					return true;
				}
			}
			in.close();
		}
		catch(IOException e) {
			
		}
		return Main.ClientExistePas(SS);
	}
	
	public ClientPermanent(long numeroDeSecuriteSociale, JFrame f){
		File Clients = new File("Clients.stk");
		BufferedReader in;
		try {
		in = new BufferedReader(new FileReader(Clients));
		String str;
		boolean b=true;
		while (((str=in.readLine()) != null)&&(b)) {
			int j=0;
			String s="";
			while(str.charAt(j)!=',') {
				s+=str.charAt(j);
				j++;
			}
			if(Long.parseLong(s)==numeroDeSecuriteSociale) {
				b=false;
				this.numeroDeSecuriteSociale=numeroDeSecuriteSociale;
				j++;
				s="";
				while(str.charAt(j)!=',') {
					s+=str.charAt(j);
					j++;
				}
				this.nom=s;
				j++;
				s="";
				while(str.charAt(j)!=',') {
					s+=str.charAt(j);
					j++;
				}
				this.prenom=s;
				j++;
				s="";
				while(str.charAt(j)!=',') {
					s+=str.charAt(j);
					j++;
				}
				this.age=Integer.parseInt(s);
				j++;
				s="";
				while(str.charAt(j)!='-') {
					s+=str.charAt(j);
					j++;
				}
				this.chronique=Boolean.parseBoolean(s);
				j++;
				String s1,s2;
				this.traitements=new HashMap<String, Integer>();
				while(j!=str.length()) {
					s1="";
					while(str.charAt(j)!=',') {
						s1+=str.charAt(j);
						j++;
					}
					j++;
					s2="";
					while(str.charAt(j)!='-') {
						s2+=str.charAt(j);
						j++;
					}
					j++;
					this.traitements.put(s1, Integer.parseInt(s2));
				}
			}
		}
		if (b) JOptionPane.showMessageDialog(f, "Le client n'existe pas !");
		in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void AjouterClient(boolean interm){ //boolean a true pour ajouter vers le fichier intermediaire et pas l'original
		File Clients ;
		if(!interm)
		Clients= new File("Clients.stk");
		else Clients = new File("Clients.int");
		try {
			FileWriter out = new FileWriter(Clients,true);
			out.write(numeroDeSecuriteSociale+","+nom+","+prenom+","+age+","+chronique+"-");
			for(String i : traitements.keySet())
				out.write(i+","+traitements.get(i)+"-");
			out.write("\r\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Acheter(boolean ord,ArrayList<MedicamentPrescrit> prescription, HashMap<String, ArrayList<MedicamentEnStock>> Stock, HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter, JFrame f) {//The String key to the hash map is Medicament.nom+;;
		try {
		long date = Donnees.Aujourdhui();
		Double payer=0.;
		ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
		ArrayList<Integer> remove = new ArrayList<Integer>();
		int r;
		for(int i=0;i<prescription.size();i++) {
			MedicamentPrescrit p = prescription.get(i);
			ArrayList<MedicamentEnStock> Meds = Stock.get(p.nom);
			boolean skip = false;
			if(Meds==null) {
				r = JOptionPane.showConfirmDialog(f, "Le medicament prescrit "+p.nom+" n'est pas disponible, \nSouhaitez-vous continuer la transaction?", "Continuer",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
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
					r = JOptionPane.showConfirmDialog(f,"Quantité insuffisante de "+p.nom+" pour accomplir la transaction,\nSouhaitez-vous passer une commande pour "+p.quantité+" unité(s) de "+p.nom+"?", "",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(r==JOptionPane.YES_OPTION)
						PasserCommande(p.nom, p.quantité, p.durée);
					r = JOptionPane.showConfirmDialog(f, "Souhaitez-vous continuer la transaction?", "Continuer?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
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
						r = JOptionPane.showConfirmDialog(f, "Aucun lot de "+p.nom+" ne respecte la durée du traitement,\nSouhaitez-vous passer une commande pour "+p.quantité+" unité(s) de "+p.nom+"?", "",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(r==JOptionPane.YES_OPTION)
							PasserCommande(p.nom, p.quantité, p.durée);
						r = JOptionPane.showConfirmDialog(f, "Souhaitez-vous continuer la transaction?", "Continuer?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
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
				if((Meds.get(j).getQuantité()>=p.quantité)&&(Meds.get(j).getDateExpiration().getTime()>=date+p.durée*86400000)) {
					System.out.println(Meds.get(j).getDateExpiration().getTime()+">="+date+p.durée*86400000);
					Meds.set(j, new MedicamentEnStock(Meds.get(j).getMedicament(), Meds.get(j).getQuantité()-p.quantité, Meds.get(j).getNumeroLot(), Meds.get(j).getDateExpiration(), Meds.get(j).getPrix()));
					payer+=(Meds.get(j).getPrix()-Meds.get(j).TauxDeRemboursement())*p.quantité;
					presc.add(new MedicamentPrescrit(p.nom,p.quantité,Meds.get(j).getNumeroLot()));
					j= Meds.size();
				}
				j++;
			}
			this.MettreAJourTraitements(prescription);
		}
		Main.TransactionSucces(payer, presc);
		Donnees.NettoyerStock(Stock,exter,inter); //Enregistrer stock se fait implicitement
		}
		catch(IOException e) {
			
		}
	}
	
	public void PasserCommande(String nomMedicament, int quantité, int durée) {
		File Commandes = new File("Commandes.stk");
		FileWriter out;
		try {
			out = new FileWriter(Commandes,true);
			out.write(nomMedicament+","+this.numeroDeSecuriteSociale+","+quantité+","+durée+"-\r\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void AnnulerCommande(String nomMedicament,HashMap<String, ArrayList<MedicamentEnStock>> Stock, HashMap<String,MedicamentEnInterne> Inter, HashMap<String,MedicamentProduitEnExterne> Exter) {
		try {
		File Commandes = new File("Commandes.stk");
		BufferedReader inC = new BufferedReader(new FileReader(Commandes));
		File interm = new File("Commandes.int");
		FileWriter outint = new FileWriter(interm);
		boolean trouvé = false;
		String str;
		while ((str=inC.readLine()) != null) {
			int j = 0;
			String med="";
			while(str.charAt(j)!=',') {
				med=med+str.charAt(j);
				j++;
			}
			if(med.equals(nomMedicament)) {
				j++;
				String cli="";
				while(str.charAt(j)!=',') {
					cli=cli+str.charAt(j);
					j++;
				}
				if(Long.parseLong(cli)==this.numeroDeSecuriteSociale) {
					trouvé=true;
					j++;
					String qt="";
					while(str.charAt(j)!=',') {
						qt+=str.charAt(j);
						j++;
					}
					ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
					presc.add(new MedicamentPrescrit(med, Integer.parseInt(qt), 0));
					Main.PopUpAnnulerCommande(cli, true, "", presc);
				}
				else outint.write(str+"\r\n");
			}
			else outint.write(str+"\r\n");
		}
		inC.close();
		outint.close();
		Commandes.delete();
		interm.renameTo(Commandes);
		if(!trouvé) {
			File Servies = new File("CommandesServies.stk");
			BufferedReader inS = new BufferedReader(new FileReader(Servies));
			File intermS = new File("CommandesServies.int");
			FileWriter outintS = new FileWriter(intermS);
			while ((str=inS.readLine()) != null) {
				int j = 0;
				String med="";
				while(str.charAt(j)!=',') {
					med=med+str.charAt(j);
					j++;
				}
				if(med.equals(nomMedicament)) {
					j++;
					String cli="";
					while(str.charAt(j)!=',') {
						cli=cli+str.charAt(j);
						j++;
					}
					if(Long.parseLong(cli)==this.numeroDeSecuriteSociale) {
						j++;
						String qt="";
						while(str.charAt(j)!=',') {
							qt+=str.charAt(j);
							j++;
						}
						j++;
						String lot="";
						while(str.charAt(j)!=',') {
							lot=lot+str.charAt(j);
							j++;
						}
						j++;
						String prix="";
						while(str.charAt(j)!=',') {
							prix+=str.charAt(j);
							j++;
						}
						j++;
						String dr="";
						while(str.charAt(j)!='-') {
							dr+=str.charAt(j);
							j++;
						}
						//----------------------------------------------------------------------------------------------------
						int i = 0;
						while((i<Stock.get(nomMedicament).size())&&(Stock.get(nomMedicament).get(i).getNumeroLot()!=Integer.parseInt(lot)))
							i++;
						if((Stock.get(nomMedicament).size()==0)||(i==Stock.get(nomMedicament).size())) {
							Medicament medd;
							if(Inter.get(nomMedicament)!=null)
								medd= Inter.get(nomMedicament);
							else medd= Exter.get(nomMedicament);
							Stock.get(nomMedicament).add(new MedicamentEnStock(medd, Integer.parseInt(qt), Integer.parseInt(lot), new Date(Long.parseLong(dr)), Double.parseDouble(prix)));
						}
						else {
							Stock.get(nomMedicament).get(i).
							setQuantité(Stock.get(nomMedicament).get(i).getQuantité()+Integer.parseInt(qt));
						}
						ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
						presc.add(new MedicamentPrescrit(med, Integer.parseInt(qt), 0));
						Main.PopUpAnnulerCommande(cli, true, "", presc);
						//----------------------------------------------------------------------------------------------------------
					}
					else outintS.write(str+"\r\n");//si client <> notre client
				}
				else outintS.write(str+"\r\n");//si med <> nomMed
			}//fin du tantque
			Donnees.EnregistrerStock(Stock,Exter,Inter);
			inS.close();
			outintS.close();
			Servies.delete();
			intermS.renameTo(Servies);
		}
		}
		catch(IOException e) {
			
		}
	}
	
	public void RetirerCommande(String nomMedicament, JFrame f){
		try{
		String str;	
		File Servies = new File("CommandesServies.stk");
		BufferedReader inS = new BufferedReader(new FileReader(Servies));
		File intermS = new File("CommandesServies.int");
		FileWriter outintS = new FileWriter(intermS);
		boolean nonTrouvé = true;
		while (((str=inS.readLine()) != null)&&(nonTrouvé)) {
			int j = 0;
			String med="";
			while(str.charAt(j)!=',') {
				med=med+str.charAt(j);
				j++;
			}
			if(med.equals(nomMedicament)) {
				j++;
				String cli="";
				while(str.charAt(j)!=',') {
					cli=cli+str.charAt(j);
					j++;
				}
				j++;
				if(Long.parseLong(cli)==this.numeroDeSecuriteSociale) {
					String qt="";
					while(str.charAt(j)!=',') {
						qt+=str.charAt(j);
						j++;
					}
					j++;
					String lot="";
					while(str.charAt(j)!=',') {
						lot=lot+str.charAt(j);
						j++;
					}
					j++;
					String prix="";
					while(str.charAt(j)!=',') {
						prix+=str.charAt(j);
						j++;
					}
					j++;
					@SuppressWarnings("unused")
					String dr="";
					while(str.charAt(j)!='-') {
						dr+=str.charAt(j);
						j++;
					}
					ArrayList<MedicamentPrescrit> presc = new ArrayList<MedicamentPrescrit>();
					presc.add(new MedicamentPrescrit(nom,Integer.parseInt(qt),Integer.parseInt(lot)));
					Double payer = Double.parseDouble(prix)*Integer.parseInt(qt);
					Main.TransactionSucces(payer, presc);
					//Mettre a jour les traitements
					ArrayList<MedicamentPrescrit> prescription = new ArrayList<MedicamentPrescrit>();
					prescription.add(new MedicamentPrescrit(med,Integer.parseInt(qt),0));
					this.MettreAJourTraitements(prescription);
					nonTrouvé=false;
				}
				else outintS.write(str+"\r\n");
			}
			else outintS.write(str+"\r\n");
		}
		inS.close();
		outintS.close();
		Servies.delete();
		intermS.renameTo(Servies);
		if(nonTrouvé)
			JOptionPane.showMessageDialog(f, "Le client s'est soit présenté sans avoir été appelé soit vous avez faite une erreur dans le nom du médicament commandé.");

		}
		catch(IOException e) {
			
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Paire<JFrame, Paire<Integer, Integer>>> ServirCommandesClients(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter){
		ArrayList<Paire<JFrame, Paire<Integer, Integer>>> list= new ArrayList<Paire<JFrame, Paire<Integer, Integer>>>();
		try {
		File Commandes = new File("Commandes.stk");
		BufferedReader in = new BufferedReader(new FileReader(Commandes));
		File Servies = new File("CommandesServies.stk");
		FileWriter out = new FileWriter(Servies,true);
		File interm = new File("Commandes.int");
		FileWriter outint = new FileWriter(interm);
		String str;
		while((str=in.readLine())!=null) {//str = nom,client,quantité-
			int j=0;
			String nom="";
			while(str.charAt(j)!=',') {
				nom+=str.charAt(j);
				j++;
			}
			j++;
			String cli="";
			while(str.charAt(j)!=',') {
				cli+=str.charAt(j);
				j++;
			}
			j++;
			String qt="";
			while(str.charAt(j)!=',') {
				qt+=str.charAt(j);
				j++;
			}
			j++;
			String dr="";
			while(str.charAt(j)!='-') {
				dr+=str.charAt(j);
				j++;
			}
			if((Stock.get(nom)!=null)&&(Stock.get(nom).size()!=0)) {
				int i =0;
				while((i<Stock.get(nom).size())&&((Stock.get(nom).get(i).getQuantité()<Integer.parseInt(qt))||(Stock.get(nom).get(i).getDateExpiration().getTime()<(long)(Integer.parseInt(dr)*86400000+Donnees.Aujourdhui())))) 
					i++;
				if((i!=Stock.get(nom).size())&&(Stock.get(nom).get(i).getQuantité()>=Integer.parseInt(qt))) {
					Stock.get(nom).get(i).setQuantité(Stock.get(nom).get(i).getQuantité()-Integer.parseInt(qt));
					out.write(nom+","+cli+","+qt+","+Stock.get(nom).get(i).getNumeroLot()+","+Stock.get(nom).get(i).getPrix()+","+Stock.get(nom).get(i).getDateExpiration().getTime()+"-");//nom,client,quantité,lot-
					out.write("\r\n");
					Paire <Integer, Integer> P = new Paire<Integer, Integer>(0, 0); 
					list.add(new Paire(Main.ServirCommandeClient(cli, nom, P),P));
				}//else y a pas de lots valables
				else 
					outint.write(str+"\r\n");
			}//else le medicament n'existe pas dans le stock
			else 
				outint.write(str+"\r\n");
		}
		Donnees.EnregistrerStock(Stock,exter,inter);
		outint.close();
		in.close();
		out.close();
		Commandes.delete();
		interm.renameTo(Commandes);
		}
		catch(IOException e) {
			
		}
		return list;
	}
	
	public void MettreAJourTraitements(ArrayList<MedicamentPrescrit> prescription) throws IOException {
		for(int i=0;i<prescription.size();i++) {
			MedicamentPrescrit p = prescription.get(i);
			if(traitements.get(p.nom)==null) 
				traitements.put(p.nom, p.quantité);
			else 
				traitements.put(p.nom,traitements.get(p.nom)+p.quantité);
			}
		//mettre a jour le fichier de clients
		File Clients = new File("Clients.stk");
		File interm = new File("Clients.int");
		BufferedReader in = new BufferedReader(new FileReader(Clients));
		FileWriter out = new FileWriter(interm,true);
		String str;
		while ((str=in.readLine()) != null) {
			int k=0;
			String ss="";
			while(str.charAt(k)!=',') {
				ss+=str.charAt(k);
				k++;
			}
			if(Long.parseLong(ss)!=numeroDeSecuriteSociale) 
				out.write(str+"\r\n");
			else {
				out.write(numeroDeSecuriteSociale+","+nom+","+prenom+","+age+","+chronique+"-");
				for(String i : traitements.keySet())
					out.write(i+","+traitements.get(i)+"-");
				out.write("\r\n");
			}
		}
		in.close();
		out.close();
		Clients.delete();
		interm.renameTo(Clients);	
	}
	
	@Override
	public String toString() {
		return "ClientPermanent [nom=" + nom + ", prenom=" + prenom + ", age=" + age + ", numeroDeSecuriteSociale="+ numeroDeSecuriteSociale + ", chronique=" + chronique + ", traitements=" + traitements + "]";
	}
}
