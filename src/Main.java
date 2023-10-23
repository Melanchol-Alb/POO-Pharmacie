import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

public class Main {
	public static void main(String[] args) throws IOException { 
		Creation();
		Initialisation(Donnees.RecupererStock(Donnees.RecupererMedicExter(), Donnees.RecupererMedicInter()),Donnees.RecupererMedicExter(),Donnees.RecupererMedicInter());
		
	}
	
	public static void Creation() {
		File clients = new File("Clients.stk");
		File Commandes = new File("Commandes.stk");
		File CommandesServies = new File("CommandesServies.stk");
		File CommandesMedecin = new File("CommandesMedecin.stk");
		File CommandesMedecinServies = new File("CommandesMedecinServies.stk");
		File Fournisseurs = new File("Fournisseurs.stk");
		File Interne = new File("Interne.stk");
		File Externe = new File("Externe.stk");
		File StockInterne = new File("StockInterne.stk");
		File StockExterne = new File("StockExterne.stk");
		try {
			clients.createNewFile();
			Commandes.createNewFile();
			CommandesServies.createNewFile();
			CommandesMedecin.createNewFile();
			CommandesMedecinServies.createNewFile();
			Fournisseurs.createNewFile();
			Interne.createNewFile();
			Externe.createNewFile();
			StockInterne.createNewFile();
			StockExterne.createNewFile();
		} catch (IOException e) {
		}
	}

	public static void Initialisation(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) {
		JFrame f = new JFrame();
		f.setTitle("Pharmacie");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(100, 100, 635, 416);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		f.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnEntreEnStock = new JButton("Entr\u00E9e en stock");
		btnEntreEnStock.setBounds(217, 73, 175, 23);
		btnEntreEnStock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EntreeEnStock(Stock, exter, inter);
			}
		});
		contentPane.add(btnEntreEnStock);
		
		JButton btnAchat = new JButton("Achat");
		btnAchat.setBounds(245, 107, 119, 23);
		btnAchat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Achat(Stock, exter, inter);
			}
		});
		contentPane.add(btnAchat);
		
		JButton btnCommande = new JButton("Commande");
		btnCommande.setBounds(245, 141, 119, 23);
		btnCommande.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PasserCommande(Stock, exter, inter);
			}
		});
		contentPane.add(btnCommande);
		
		JButton btnServirLesCommandes = new JButton("Gestion des commandes");
		btnServirLesCommandes.setBounds(217, 175, 175, 23);
		btnServirLesCommandes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GestionCommandes(Stock, exter, inter);
			}
		});
		contentPane.add(btnServirLesCommandes);
		
		JButton btnAfficherLesCommandes = new JButton("Afficher les commandes");
		btnAfficherLesCommandes.setBounds(217, 209, 175, 23);
		btnAfficherLesCommandes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AfficherCommandes();
			}
		});
		contentPane.add(btnAfficherLesCommandes);
		
		JButton btnAfficherLesClients = new JButton("Afficher les clients");
		btnAfficherLesClients.setBounds(217, 243, 175, 23);
		btnAfficherLesClients.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AfficherClients();
			}
		});
		contentPane.add(btnAfficherLesClients);
		f.setVisible(true);
	}

	//Ce qui suit est pour assurer les entrées en stock de medicament en interne et produits en externe
	public static void EntreeEnStock(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) {
			JFrame f = new JFrame();
			f.setTitle("Entr\u00E9e en stock");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 560, 420);
			JPanel contentPane;
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			//Formats des champs
			NumberFormat format = NumberFormat.getInstance();
			NumberFormatter entier= new NumberFormatter(format);
			entier.setValueClass(Integer.class);
			entier.setMinimum(0);
			entier.setMaximum(Integer.MAX_VALUE);
			entier.setAllowsInvalid(false);
			
			NumberFormat format2 = new DecimalFormat("0.00");
			NumberFormatter réel= new NumberFormatter(format2);
			réel.setValueClass(Double.class);
			réel.setMinimum(0.);
			réel.setMaximum(Double.MAX_VALUE);
			réel.setAllowsInvalid(false);
			
			SimpleDateFormat format3 = new SimpleDateFormat("dd.MM.yyyy");
			DateFormatter dates = new DateFormatter(format3);
			//Contenus
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(10, 11, 524, 360);
			contentPane.add(tabbedPane);
			
			JPanel Externe = new JPanel();
			tabbedPane.addTab("M\u00E9dicament produit en externe", null, Externe, null);
			Externe.setLayout(null);
			
			JLabel lblNomDuMedicament = new JLabel("Nom du m\u00E9dicament:");
			lblNomDuMedicament.setBounds(106, 129, 140, 14);
			Externe.add(lblNomDuMedicament);
			
			JComboBox<String> textFieldNom = new JComboBox<String>();
			textFieldNom.setBounds(254, 126, 143, 20);
			Externe.add(textFieldNom);
			textFieldNom.setEditable(true);
			for(String i : exter.keySet())
				textFieldNom.addItem(i);
			
			JLabel lblQuantit = new JLabel("Quantit\u00E9:");
			lblQuantit.setBounds(127, 186, 119, 14);
			Externe.add(lblQuantit);
			
			JFormattedTextField textFieldQuantité = new JFormattedTextField(entier);
			textFieldQuantité.setBounds(254, 183, 86, 20);
			Externe.add(textFieldQuantité);
			textFieldQuantité.setColumns(10);
			
			JLabel lblNumroDuLot = new JLabel("Num\u00E9ro du lot:");
			lblNumroDuLot.setBounds(127, 211, 119, 14);
			Externe.add(lblNumroDuLot);
			
			JFormattedTextField textFieldNumeroLot = new JFormattedTextField(entier);
			textFieldNumeroLot.setBounds(254, 208, 86, 20);
			Externe.add(textFieldNumeroLot);
			textFieldNumeroLot.setColumns(10);
			
			JLabel lblDateDePremption = new JLabel("Date de p\u00E9remption:");
			lblDateDePremption.setBounds(127, 236, 119, 14);
			Externe.add(lblDateDePremption);
			
			JFormattedTextField textFieldDatePeremption = new JFormattedTextField(dates);
			textFieldDatePeremption.setBounds(254, 233, 86, 20);
			Externe.add(textFieldDatePeremption);
			textFieldDatePeremption.setColumns(10);
			
			JLabel lblPrix = new JLabel("Prix:");
			lblPrix.setBounds(127, 261, 119, 14);
			Externe.add(lblPrix);
			
			JFormattedTextField textFieldPrix = new JFormattedTextField(réel);
			textFieldPrix.setBounds(254, 258, 86, 20);
			Externe.add(textFieldPrix);
			textFieldPrix.setColumns(10);
			
			JLabel lblType = new JLabel("Type:");
			lblType.setBounds(26, 157, 46, 14);
			Externe.add(lblType);
			
			JComboBox<String> listType = new JComboBox<String>(Donnees.getTypes());
			listType.setBounds(70, 157, 91, 18);
			Externe.add(listType);
			
			JLabel lblModeDePrise = new JLabel("Mode de prise:");
			lblModeDePrise.setBounds(171, 157, 86, 14);
			Externe.add(lblModeDePrise);
			
			JComboBox<String> listMode = new JComboBox<String>(Donnees.getModes());
			listMode.setBounds(273, 156, 82, 18);
			Externe.add(listMode);
			
			JCheckBox chckbxSousOrdonnance = new JCheckBox("Ordonnance");
			chckbxSousOrdonnance.setBounds(380, 153, 119, 23);
			Externe.add(chckbxSousOrdonnance);
			
			JLabel lblNomDuFournisseur = new JLabel("Nom du fournisseur:");
			lblNomDuFournisseur.setBounds(106, 28, 140, 14);
			Externe.add(lblNomDuFournisseur);
			
			JTextField textFieldFournisseur = new JTextField();
			textFieldFournisseur.setBounds(254, 25, 143, 20);
			Externe.add(textFieldFournisseur);
			textFieldFournisseur.setColumns(10);
			
			JLabel lblAdresseMail = new JLabel("Email:");
			lblAdresseMail.setBounds(26, 75, 70, 14);
			lblAdresseMail.setEnabled(false);
			Externe.add(lblAdresseMail);
			
			JTextField textFieldMail = new JTextField();
			textFieldMail.setBounds(88, 72, 169, 20);
			textFieldMail.setEnabled(false);
			Externe.add(textFieldMail);
			textFieldMail.setColumns(10);
			
			JLabel lblNumero = new JLabel("Num\u00E9ro:");
			lblNumero.setBounds(273, 75, 70, 14);
			lblNumero.setEnabled(false);
			Externe.add(lblNumero);
			
			JTextField textFieldNumero = new JTextField();
			textFieldNumero.setBounds(340, 72, 120, 20);
			textFieldNumero.setEnabled(false);
			Externe.add(textFieldNumero);
			textFieldNumero.setColumns(10);
			
			JCheckBox chckbxNouveauFournisseur = new JCheckBox("Nouveau fournisseur:");
			chckbxNouveauFournisseur.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					lblAdresseMail.setEnabled(chckbxNouveauFournisseur.isSelected());
					textFieldMail.setEnabled(chckbxNouveauFournisseur.isSelected());
					lblNumero.setEnabled(chckbxNouveauFournisseur.isSelected());
					textFieldNumero.setEnabled(chckbxNouveauFournisseur.isSelected());
				}
			});
			chckbxNouveauFournisseur.setBounds(178, 49, 162, 23);
			Externe.add(chckbxNouveauFournisseur);
			
			JButton btnEnregistrer = new JButton("Enregistrer");
			btnEnregistrer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						if((chckbxNouveauFournisseur.isSelected())&&(Fournisseur.existe(textFieldFournisseur.getText()))) {
							//Le fournisseur existe déjà
							textFieldFournisseur.setText(textFieldFournisseur.getText()+"!!!");
							return ;
						}
						if((!chckbxNouveauFournisseur.isSelected())&&(!Fournisseur.existe(textFieldFournisseur.getText()))) {
							//Le fournisseur n'existe pas
							textFieldFournisseur.setText(textFieldFournisseur.getText()+"!!!");
							return ;
						}
						Fournisseur fr;
						if(chckbxNouveauFournisseur.isSelected()) {
							fr=new Fournisseur(textFieldFournisseur.getText(),textFieldMail.getText(),textFieldNumero.getText());
						}
						else fr= new Fournisseur(textFieldFournisseur.getText());
						if(!exter.containsKey((String)textFieldNom.getSelectedItem()))
							exter.put((String)textFieldNom.getSelectedItem(), new MedicamentProduitEnExterne((String)textFieldNom.getSelectedItem(), (String)listType.getSelectedItem(), (String)listMode.getSelectedItem(), chckbxSousOrdonnance.isSelected(), new ArrayList<Fournisseur>()));
						fr.fournis(new MedicamentEnStock(exter.get((String)textFieldNom.getSelectedItem()),Integer.parseInt(textFieldQuantité.getText().replaceAll(" ", "")),Integer.parseInt(textFieldNumeroLot.getText().replaceAll(" ", "")),format3.parse(textFieldDatePeremption.getText()),Double.parseDouble(textFieldPrix.getText().replaceAll(" ", "").replaceFirst(",", "."))), exter, Stock, inter);
						JOptionPane.showMessageDialog(f, "Entrée en stock effectuée avec succès!");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnEnregistrer.setBounds(208, 289, 112, 23);
			Externe.add(btnEnregistrer);
			
			/////////////////////////Interne////////////////////////////////////////////////////////////////////////////////
			JPanel Interne = new JPanel();
			tabbedPane.addTab("M\u00E9dicament en interne", null, Interne, null);
			Interne.setLayout(null);
			
			JLabel labelPrix2 = new JLabel("Prix:");
			labelPrix2.setBounds(133, 256, 127, 14);
			Interne.add(labelPrix2);
			
			JFormattedTextField textFieldPrix2 = new JFormattedTextField(réel);
			textFieldPrix2.setBounds(268, 253, 86, 20);
			textFieldPrix2.setColumns(10);
			Interne.add(textFieldPrix2);
			
			JFormattedTextField textFieldDatePeremption2 = new JFormattedTextField(dates);
			textFieldDatePeremption2.setBounds(268, 228, 86, 20);
			textFieldDatePeremption2.setColumns(10);
			Interne.add(textFieldDatePeremption2);
			
			JLabel labelDatePeremption2 = new JLabel("Date de p\u00E9remption:");
			labelDatePeremption2.setBounds(133, 231, 127, 14);
			Interne.add(labelDatePeremption2);
			
			JLabel labelNumeroLot2 = new JLabel("Num\u00E9ro du lot:");
			labelNumeroLot2.setBounds(133, 206, 127, 14);
			Interne.add(labelNumeroLot2);
			
			JFormattedTextField textFieldNumeroLot2 = new JFormattedTextField(entier);
			textFieldNumeroLot2.setBounds(268, 203, 86, 20);
			textFieldNumeroLot2.setColumns(10);
			Interne.add(textFieldNumeroLot2);
			
			JFormattedTextField textFieldQuantité2 = new JFormattedTextField(entier);
			textFieldQuantité2.setBounds(268, 178, 86, 20);
			textFieldQuantité2.setColumns(10);
			Interne.add(textFieldQuantité2);
			
			JLabel labelQuantité2 = new JLabel("Quantit\u00E9:");
			labelQuantité2.setBounds(133, 181, 127, 14);
			Interne.add(labelQuantité2);
			
			JComboBox<String> listType2 = new JComboBox<String>(Donnees.getTypes());
			listType2.setBounds(69, 83, 90, 18);
			Interne.add(listType2);
			
			JLabel labelType2 = new JLabel("Type:");
			labelType2.setBounds(24, 84, 46, 14);
			Interne.add(labelType2);
			
			JLabel labelMode2 = new JLabel("Mode de prise:");
			labelMode2.setBounds(169, 84, 86, 14);
			Interne.add(labelMode2);
			
			JComboBox<String> listMode2 = new JComboBox<String>(Donnees.getModes());
			listMode2.setBounds(271, 83, 82, 18);
			Interne.add(listMode2);
			
			JCheckBox chckbxOrdonnance2 = new JCheckBox("Ordonnance");
			chckbxOrdonnance2.setBounds(378, 80, 110, 23);
			Interne.add(chckbxOrdonnance2);
			
			JTable tableIngrédiants = new JTable();
			tableIngrédiants.setModel(new DefaultTableModel(new Object[][] {},new String[] {"Ingr\u00E9dient", "Dosage"}));
			tableIngrédiants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultTableModel model = (DefaultTableModel) tableIngrédiants.getModel();
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(34, 107, 419, 60);
			Interne.add(scrollPane);
			scrollPane.setViewportView(tableIngrédiants);
			
			JButton plus = new JButton("+");
			plus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					model.addRow(new Object[] {"",0.});
				}
			});
			plus.setBounds(463, 110, 46, 23);
			Interne.add(plus);
			
			JButton moins = new JButton("-");
			moins.setBounds(463, 142, 46, 23);
			moins.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					model.removeRow(tableIngrédiants.getSelectedRow());
				}
			});
			Interne.add(moins);
			
			JLabel labelNom2 = new JLabel("Nom du m\u00E9dicament:");
			labelNom2.setBounds(112, 55, 126, 14);
			Interne.add(labelNom2);
			
			JComboBox<String> textFieldNom2 = new JComboBox<String>();
			textFieldNom2.setBounds(247, 52, 143, 20);
			textFieldNom2.setEditable(true);
			for(String i : inter.keySet())
				textFieldNom2.addItem(i);
			Interne.add(textFieldNom2);
			
			JButton btnEnregistrer2 = new JButton("Enregistrer");
			btnEnregistrer2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						ArrayList<Paire<String, Double>> ingreds = new ArrayList<Paire<String, Double>>();
						int i=0;
						while(i<tableIngrédiants.getRowCount()) {
							String Nom = (String)model.getValueAt(i, 0),dose= (String)model.getValueAt(i, 1);
							for(int j=0;j<dose.length();j++) {
								if((dose.charAt(j)!='0')&&(dose.charAt(j)!='1')&&(dose.charAt(j)!='2')&&(dose.charAt(j)!='3')&&(dose.charAt(j)!='4')&&(dose.charAt(j)!='5')&&(dose.charAt(j)!='6')&&(dose.charAt(j)!='7')&&(dose.charAt(j)!='8')&&(dose.charAt(j)!='9')&&(dose.charAt(j)!='.')) {
									//Dosage non numérique
									return;
								}
							}
							ingreds.add(new Paire<String, Double>(Nom, Double.parseDouble(dose)));
							i++;
						}
						double Dose=0;
						i=0;
						while(i<tableIngrédiants.getRowCount()) {
							Dose+=Double.parseDouble((String)model.getValueAt(i, 1));
							i++;
						}
						if(Dose!=100.) {
							JOptionPane.showMessageDialog(f, "Erreur dans le dosage!! Somme <> 100");
							return;
						}
						MedicamentEnInterne m ;
						if(!inter.containsKey((String)textFieldNom2.getSelectedItem()))
							m= new MedicamentEnInterne((String)textFieldNom2.getSelectedItem(),(String)listType.getSelectedItem(), (String)listMode.getSelectedItem(), chckbxSousOrdonnance.isSelected(), ingreds);
						else m= new MedicamentEnInterne((String)textFieldNom2.getSelectedItem(), inter.get((String)textFieldNom2.getSelectedItem()).getType(), inter.get((String)textFieldNom2.getSelectedItem()).getMode(), inter.get((String)textFieldNom2.getSelectedItem()).isOrdonnance(), inter.get((String)textFieldNom2.getSelectedItem()).ingredients);
						MedicamentEnStock med = new MedicamentEnStock(m,Integer.parseInt(textFieldQuantité2.getText().replaceAll(" ", "")),Integer.parseInt(textFieldNumeroLot2.getText().replaceAll(" ", "")),format3.parse(textFieldDatePeremption2.getText()),Double.parseDouble(textFieldPrix2.getText().replaceAll(" ", "").replaceAll(",", ".")));
						Donnees.AjoutStockInterne(med, inter, Stock, exter);
						JOptionPane.showMessageDialog(f, "Entrée en stock effectuée avec succès!");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnEnregistrer2.setBounds(202, 284, 120, 23);
			Interne.add(btnEnregistrer2);
			f.setVisible(true);
		}
	//Ce qui suit est pour l'affichage des commandes
	public static void AfficherCommandes() {
			JFrame f = new JFrame();
			f.setTitle("Affichage des commandes");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 650, 355);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(10, 11, 614, 295);
			contentPane.add(tabbedPane);
			
			JTabbedPane tabbedPaneClients = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("Clients permanents", null, tabbedPaneClients, null);
			
			JPanel panelClient = new JPanel();
			tabbedPaneClients.addTab("Commandes non servies", null, panelClient, null);
			panelClient.setLayout(null);
			
			JScrollPane scrollPaneCNSC = new JScrollPane();
			scrollPaneCNSC.setBounds(10, 11, 584, 217);
			panelClient.add(scrollPaneCNSC);
			
			JTable tableCNSC = new JTable();
			tableCNSC.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"M\u00E9dicament", "S\u00E9curit\u00E9 sociale", "Quantit\u00E9", "Dur\u00E9e (jours)"
				}
			) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			tableCNSC.setRowSelectionAllowed(false);
			tableCNSC.setDefaultEditor(Object.class, null);
			tableCNSC.setBounds(257, 6, 0, 0);
			scrollPaneCNSC.setViewportView(tableCNSC);
			
			JPanel panelClientsServies = new JPanel();
			tabbedPaneClients.addTab("Commandes servies", null, panelClientsServies, null);
			panelClientsServies.setLayout(null);
			
			JScrollPane scrollPaneCSC = new JScrollPane();
			scrollPaneCSC.setBounds(10, 11, 584, 217);
			panelClientsServies.add(scrollPaneCSC);
			
			JTable tableCSC = new JTable();
			tableCSC.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"M\u00E9dicament", "S\u00E9curit\u00E9 sociale", "Quantit\u00E9", "Lot", "Prix", "Date p\u00E9remption"
				}
			) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Object.class, String.class, String.class, String.class, String.class, String.class
				};
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			tableCSC.setRowSelectionAllowed(false);
			tableCSC.setDefaultEditor(Object.class, null);
			tableCSC.setBounds(0, 0, 1, 1);
			scrollPaneCSC.setViewportView(tableCSC);
			
			JTabbedPane tabbedPaneMédecins = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("M\u00E9decins conventionn\u00E9s", null, tabbedPaneMédecins, null);
			
			JPanel panelCliniques = new JPanel();
			tabbedPaneMédecins.addTab("Commandes non servies", null, panelCliniques, null);
			panelCliniques.setLayout(null);
			
			JScrollPane scrollPaneCNSM = new JScrollPane();
			scrollPaneCNSM.setBounds(10, 11, 584, 217);
			panelCliniques.add(scrollPaneCNSM);
			
			JTable tableCNSM = new JTable();
			tableCNSM.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Code", "M\u00E9decin", "Clinique", "M\u00E9dicaments"
				}
			));
			tableCNSM.setRowSelectionAllowed(false);
			tableCNSM.setDefaultEditor(Object.class, null);
			scrollPaneCNSM.setViewportView(tableCNSM);
			
			Action actCNSM = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					JTable table = (JTable)e.getSource();
					int row = Integer.valueOf(e.getActionCommand());
					File commandes = new File("CommandesMedecin.stk");
					try {
						BufferedReader in = new BufferedReader(new FileReader(commandes));
						String str="",code="";
						while(((code.equals("")&&(str=in.readLine())!=null))) {
							int i = str.length()-1;
							while(str.charAt(i)!=',') {
								code=str.charAt(i)+code;
								i--;
							}
							if(!code.equals(((DefaultTableModel)table.getModel()).getValueAt(row, 0)))
								code="";
						}
						ListerMeds(false, str);
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			};
			@SuppressWarnings("unused")
			ButtonColumn x = new ButtonColumn(tableCNSM,actCNSM,3);
			
			JPanel panel_CliniquesServies = new JPanel();
			tabbedPaneMédecins.addTab("Commandes servies", null, panel_CliniquesServies, null);
			panel_CliniquesServies.setLayout(null);
			
			JScrollPane scrollPaneCSM = new JScrollPane();
			scrollPaneCSM.setBounds(10, 11, 584, 217);
			panel_CliniquesServies.add(scrollPaneCSM);
			
			JTable tableCSM = new JTable();
			tableCSM.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Code", "M\u00E9decin", "Clinique", "M\u00E9dicaments"
				}
			));
			tableCSM.setRowSelectionAllowed(false);
			tableCSM.setDefaultEditor(Object.class, null);
			Action actCSM = new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					JTable table = (JTable)e.getSource();
					int row = Integer.valueOf(e.getActionCommand());
					File commandes = new File("CommandesMedecinServies.stk");
					try {
						BufferedReader in = new BufferedReader(new FileReader(commandes));
						String str="",code="";
						while(((code.equals("")&&(str=in.readLine())!=null))) {
							int i = str.length()-1;
							while(str.charAt(i)!=',') {
								code=str.charAt(i)+code;
								i--;
							}
							if(!code.equals(((DefaultTableModel)table.getModel()).getValueAt(row, 0)))
								code="";
						}
						ListerMeds(true, str);
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			};
			@SuppressWarnings("unused")
			ButtonColumn y = new ButtonColumn(tableCSM,actCSM,3);
			try {
				RemplirTable("CSC", tableCSC);
				RemplirTable("CNSC", tableCNSC);
				RemplirTable("CSM", tableCSM);
				RemplirTable("CNSM", tableCNSM);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			scrollPaneCSM.setViewportView(tableCSM);
			
			f.setVisible(true);
		}
		
	public static void RemplirTable(String tab,JTable table) throws IOException{
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			SimpleDateFormat format3 = new SimpleDateFormat("dd.MM.yyyy");
			if(tab.equals("CNSC")||tab.equals("CSC")) {
				File Commandes;
				if(tab.equals("CNSC"))
					Commandes = new File("Commandes.stk");
				else Commandes = new File("CommandesServies.stk");
				BufferedReader in = new BufferedReader(new FileReader(Commandes));
				String str;
				while((str=in.readLine())!=null) {
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
					String[] vals;
					if(tab.equals("CNSC")) {
					String dr="";
					while(str.charAt(j)!='-') {
						dr+=str.charAt(j);
						j++;
					}
					j++;
					vals = new String[4];
					vals[3]=dr;
					}
					else {
						String lot="";
						while(str.charAt(j)!=',') {
							lot+=str.charAt(j);
							j++;
						}
						j++;
						String prix="";
						while(str.charAt(j)!=',') {
							prix+=str.charAt(j);
							j++;
						}
						j++;
						String dateP="";
						while(str.charAt(j)!='-') {
							dateP+=str.charAt(j);
							j++;
						}
						j++;
						vals = new String[6];
						vals[3]=lot;vals[4]=prix;vals[5]=format3.format(new Date(Long.parseLong(dateP)));
					}
					vals[0]=nom;vals[1]=cli;vals[2]=qt;
					model.addRow(vals);
				}
				in.close();
			}
			else{
				File Commandes=null;
				if(tab.equals("CNSM")) 
					Commandes = new File("CommandesMedecin.stk");
				else Commandes = new File("CommandesMedecinServies.stk");
				BufferedReader in = new BufferedReader(new FileReader(Commandes));
				String str;
				while((str=in.readLine())!=null) {
					int i=str.length()-1;
					String code = "";
					while(str.charAt(i)!=',') {
						code = str.charAt(i)+code;
						i--;
					}
					i--;
					String clin = "";
					while(str.charAt(i)!=',') {
						clin = str.charAt(i)+clin;
						i--;
					}
					i--;
					String nom = "";
					while(str.charAt(i)!='|') {
						nom = str.charAt(i)+nom;
						i--;
					}
					String[] row = new String[4];
					row[0]=code;row[1]=nom;row[2]=clin;row[3]="Lister";
					model.addRow(row);
				}
				in.close();
			}
		}
		
	public static void ListerMeds(boolean Servie,String str) {
			JFrame f = new JFrame();
			f.setTitle("M\u00E9dicaments command\u00E9s");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 450, 300);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 59, 414, 192);
			contentPane.add(scrollPane);
			
			JTable table = new JTable();
			table.setDefaultEditor(Object.class, null);
			if(Servie)
				table.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9", "Lot", "Prix", "Date péremption"}));
			else
				table.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9"}));
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			table.setRowSelectionAllowed(false);
			scrollPane.setViewportView(table);
			
			JLabel labelCommande = new JLabel("");
			labelCommande.setBounds(104, 34, 234, 14);
			contentPane.add(labelCommande);
			
			JLabel lblCode = new JLabel("Code:");
			lblCode.setBounds(25, 34, 46, 14);
			contentPane.add(lblCode);
			
			JLabel lblClinique = new JLabel("Clinique");
			lblClinique.setBounds(25, 11, 46, 14);
			contentPane.add(lblClinique);
			
			JLabel labelNom = new JLabel("");
			labelNom.setBounds(104, 11, 320, 14);
			contentPane.add(labelNom);
			
			int i=str.length()-1;
			while(str.charAt(i)!=',') {
				labelCommande.setText(str.charAt(i)+labelCommande.getText());
				i--;
			}
			i--;
			while(str.charAt(i)!=',') {
				labelNom.setText(str.charAt(i)+labelNom.getText());
				i--;
			}
			i--;
			
			i=0;
			if(Servie)
				while(str.charAt(i)!='|') {
					String lot="";
					while(str.charAt(i)!=',') {
						lot+=str.charAt(i);
						i++;
					}
					i++;
					String med="";
					while(str.charAt(i)!=',') {
						med+=str.charAt(i);
						i++;
					}
					i++;
					String quant="";
					while(str.charAt(i)!=',') {
						quant+=str.charAt(i);
						i++;
					}
					i++;
					String prix="";
					while(str.charAt(i)!=',') {
						prix+=str.charAt(i);
						i++;
					}
					i++;
					String dateP="";
					while(str.charAt(i)!='-') {
						dateP+=str.charAt(i);
						i++;
					}
					i++;
					String[] row = new String[5];
					SimpleDateFormat format3 = new SimpleDateFormat("dd.MM.yyyy");
					row[0]=med;row[1]=quant;row[2]=lot;row[3]=prix;row[4]=format3.format(new Date(Long.parseLong(dateP)));
					model.addRow(row);
				}
			else
				while(str.charAt(i)!='|') {
					String med="";
					while(str.charAt(i)!=',') {
						med+=str.charAt(i);
						i++;
					}
					i++;
					String quant="";
					while(str.charAt(i)!='-') {
						quant+=str.charAt(i);
						i++;
					}
					i++;
					String[] row = new String[2];
					row[0]=med;row[1]=quant;
					model.addRow(row);
				}
			
			f.setVisible(true);
		}
	//Ce qui suit est pour l'affichage des clients
	public static void AfficherClients() {
			JFrame f = new JFrame();
			f.setTitle("Afficher les clients permanents");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 700, 300);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 674, 250);
			contentPane.add(scrollPane);
			
			JTable table = new JTable();
			table.setModel(new DefaultTableModel(new Object[][] {},new String[] {"S\u00E9curit\u00E9 sociale", "Nom", "Prenom", "Age", "Chronique", "Traitements"}));
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			table.setRowSelectionAllowed(false);
			table.setDefaultEditor(Object.class, null);
			scrollPane.setViewportView(table);
			
			File Clients = new File("Clients.stk");
			try {
				BufferedReader in = new BufferedReader(new FileReader(Clients));
				String str;
				while((str=in.readLine())!=null) {
					int j=0;
					String ss="";
					while(str.charAt(j)!=',') {
						ss+=str.charAt(j);
						j++;
					}
					j++;
					String nom="";
					while(str.charAt(j)!=',') {
						nom+=str.charAt(j);
						j++;
					}
					j++;
					String prenom="";
					while(str.charAt(j)!=',') {
						prenom+=str.charAt(j);
						j++;
					}
					j++;
					String age="";
					while(str.charAt(j)!=',') {
						age+=str.charAt(j);
						j++;
					}
					j++;
					String chron="";
					while(str.charAt(j)!='-') {
						chron+=str.charAt(j);
						j++;
					}
					String[] row = {ss,nom,prenom,age,chron,"Lister"};
					model.addRow(row);
				}
				in.close();
				Action act = new AbstractAction() {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						JTable table = (JTable)e.getSource();
						int row = Integer.valueOf(e.getActionCommand());
						String ss = (String)((DefaultTableModel)table.getModel()).getValueAt(row, 0);
						try {
							ClientPermanent cl = new ClientPermanent(Long.parseLong(ss),f);
						
						JFrame f = new JFrame();
						f.setTitle("M\u00E9dicaments command\u00E9s");
						f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						f.setBounds(100, 100, 450, 300);
						JPanel contentPane = new JPanel();
						contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
						f.setContentPane(contentPane);
						contentPane.setLayout(null);
						
						JScrollPane scrollPane = new JScrollPane();
						scrollPane.setBounds(10, 59, 414, 192);
						contentPane.add(scrollPane);
						
						JTable tab = new JTable();
						tab.setDefaultEditor(Object.class, null);
						tab.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9"}));
						DefaultTableModel model = (DefaultTableModel)tab.getModel();
						tab.setRowSelectionAllowed(false);
						scrollPane.setViewportView(tab);
						
						JLabel labelSS = new JLabel("");
						labelSS.setBounds(104, 34, 234, 14);
						contentPane.add(labelSS);
						labelSS.setText(ss);
						
						JLabel lblSS = new JLabel("NumSS:");
						lblSS.setBounds(25, 34, 46, 14);
						contentPane.add(lblSS);
						
						JLabel lblNom = new JLabel("Client:");
						lblNom.setBounds(25, 11, 46, 14);
						contentPane.add(lblNom);
						
						JLabel labelNom = new JLabel("");
						labelNom.setBounds(104, 11, 320, 14);
						contentPane.add(labelNom);
						labelNom.setText(cl.getNomComp());
						
						for(String i : cl.getTraitements().keySet()) {
							String[] tr = {i,cl.getTraitements().get(i).toString()};
							model.addRow(tr);
						}
						
						f.setVisible(true);
						
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						}
					}
				};
				@SuppressWarnings("unused")
				ButtonColumn x = new ButtonColumn(table,act,5);
			} catch (IOException e) {
				e.printStackTrace();
			}
			f.setVisible(true);
		}
	//Ce qui suit est pour passer le commandes de medecins
	public static void PasserCommande(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) {
			JFrame f = new JFrame();
			f.setTitle("Passer commande");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 529, 321);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel lblNomDuMdecin = new JLabel("Nom du m\u00E9decin:");
			lblNomDuMdecin.setBounds(30, 14, 110, 14);
			contentPane.add(lblNomDuMdecin);
			
			JLabel lblClinique = new JLabel("Clinique:");
			lblClinique.setBounds(30, 39, 110, 14);
			contentPane.add(lblClinique);
			
			JTextField textFieldNom = new JTextField();
			textFieldNom.setBounds(150, 11, 291, 20);
			contentPane.add(textFieldNom);
			textFieldNom.setColumns(10);
			
			JTextField textFieldClinique = new JTextField();
			textFieldClinique.setBounds(150, 36, 291, 20);
			contentPane.add(textFieldClinique);
			textFieldClinique.setColumns(10);
			
			JScrollPane scrollPaneCommande = new JScrollPane();
			scrollPaneCommande.setBounds(61, 75, 343, 197);
			contentPane.add(scrollPaneCommande);
			
			JTable tableCommande = new JTable();
			tableCommande.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9"}) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Object.class, Integer.class
				};
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			DefaultTableModel model = (DefaultTableModel)tableCommande.getModel();
			TableColumn ColMed = tableCommande.getColumnModel().getColumn(0);
			JComboBox<String> box = new JComboBox<String>();
			for(String i : Stock.keySet())
				box.addItem(i);
			ColMed.setCellEditor(new DefaultCellEditor(box));
			scrollPaneCommande.setViewportView(tableCommande);
			
			JButton plus = new JButton("+");
			plus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					model.addRow(new Object[] {"",0});
				}
			});
			plus.setBounds(10, 120, 43, 23);
			contentPane.add(plus);
			
			JButton minus = new JButton("-");
			minus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					model.removeRow(tableCommande.getSelectedRow());
				}
			});
			minus.setBounds(10, 154, 43, 23);
			contentPane.add(minus);
			
			JButton btnEnregistrer = new JButton("OK");
			btnEnregistrer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					lblClinique.setText(model.getValueAt(0, 1).getClass().getName());
					ArrayList<Paire<String, Integer>> meds = new ArrayList<Paire<String, Integer>>();
					MedecinConventionne medecin = new MedecinConventionne(textFieldClinique.getText(),textFieldNom.getText());
					for (int i = 0; i < model.getRowCount(); i++) {
						meds.add(new Paire<String, Integer>((String)model.getValueAt(i, 0),Integer.parseInt(model.getValueAt(i, 1).toString())));
					}
						medecin.PlacerCommande(meds, f);
				}
			});
			btnEnregistrer.setBounds(414, 234, 89, 23);
			contentPane.add(btnEnregistrer);
			f.setVisible(true);
		}
	//Ce qui suit est pour les achats
	public static void TransactionSucces(double payer, ArrayList<MedicamentPrescrit> presc) {
			JFrame f = new JFrame();
			f.setTitle("Succ\u00E8s de la transaction");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 450, 300);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(36, 27, 368, 187);
			contentPane.add(scrollPane);
			
			JTable table = new JTable();
			table.setDefaultEditor(Object.class, null);
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"M\u00E9dicament", "Lot", "Quantit\u00E9"
				}
			));
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			for(int i=0;i<presc.size();i++) 
				model.addRow(new Object[] {presc.get(i).nom,presc.get(i).durée, presc.get(i).quantité}); //le numero de lot sera passé dans .durée
			scrollPane.setViewportView(table);
			
			JLabel lblLePrixTotal = new JLabel("Le prix total \u00E0 payer est : "+payer);
			lblLePrixTotal.setBounds(46, 225, 358, 14);
			contentPane.add(lblLePrixTotal);
			
			f.setVisible(true);
		}
		
	public static boolean ClientExistePas(Long SS) {
			JFrame f = new JFrame();
			f.setResizable(true);
			f.setTitle("Le client n'existe pas !!");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 450, 170);
			f.setResizable(false);
			f.getContentPane().setLayout(null);
			
			JLabel label = new JLabel("");
			label.setBounds(58, 54, 329, 14);
			label.setText("Le client dont le numero de sécurité sociale est "+SS+" n'est pas enregistré !!");
			f.getContentPane().add(label);	
			f.setVisible(true);
			return false;
		}

	public static void Achat(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) {
			JFrame f = new JFrame();
			f.setTitle("Achat");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 700, 400);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel lblNumroDeScurit = new JLabel("Num\u00E9ro de s\u00E9curit\u00E9 sociale:");
			lblNumroDeScurit.setBounds(169, 21, 166, 14);
			contentPane.add(lblNumroDeScurit);
			lblNumroDeScurit.setEnabled(false);
			
			NumberFormatter SS= new NumberFormatter(NumberFormat.getNumberInstance());
			SS.setMinimum(111111111111111L);
			SS.setMaximum(999999999999999L);
			
			JFormattedTextField textFieldSS = new JFormattedTextField(SS);
			textFieldSS.setBounds(345, 18, 160, 20);
			contentPane.add(textFieldSS);
			textFieldSS.setColumns(10);
			textFieldSS.setEnabled(false);
			
			JButton btnNouveauClient = new JButton("Nouveau Client");
			btnNouveauClient.setBounds(538, 17, 136, 23);
			btnNouveauClient.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					PopUpAjoutClient();
				}
			});
			btnNouveauClient.setEnabled(false);
			contentPane.add(btnNouveauClient);
			
			JScrollPane scrollPaneAchats = new JScrollPane();
			scrollPaneAchats.setBounds(89, 89, 447, 259);
			contentPane.add(scrollPaneAchats);
			
			JTable tableAchats = new JTable();
			tableAchats.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9", "Durée (jours)"}) {
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {Object.class, Integer.class, Integer.class};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public Class getColumnClass(int columnIndex) {return columnTypes[columnIndex];}});
			TableColumn ColMed = tableAchats.getColumnModel().getColumn(0);
			JComboBox<String> box = new JComboBox<String>();
			for(String i : Stock.keySet())
				box.addItem(i);
			ColMed.setCellEditor(new DefaultCellEditor(box));
			DefaultTableModel model = (DefaultTableModel) tableAchats.getModel();
			scrollPaneAchats.setViewportView(tableAchats);
			
			JCheckBox chckbxOrdonnance = new JCheckBox("Ordonnance");
			chckbxOrdonnance.setSelected(true);
			chckbxOrdonnance.setBounds(179, 48, 130, 23);
			contentPane.add(chckbxOrdonnance);
			chckbxOrdonnance.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					if(chckbxOrdonnance.isSelected()) {
						tableAchats.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9", "Durée (jours)"}) {
								private static final long serialVersionUID = 1L;
								@SuppressWarnings("rawtypes")
								Class[] columnTypes = new Class[] {Object.class, Integer.class, Integer.class};
								@SuppressWarnings({ "rawtypes", "unchecked" })
								@Override
								public Class getColumnClass(int columnIndex) {return columnTypes[columnIndex];}});
						TableColumn ColMed = tableAchats.getColumnModel().getColumn(0);
						ColMed.setCellEditor(new DefaultCellEditor(box));
					}
					else {
						tableAchats.setModel(new DefaultTableModel(new Object[][] {},new String[] {"M\u00E9dicament", "Quantit\u00E9"}) {
								private static final long serialVersionUID = 1L;
								@SuppressWarnings("rawtypes")
								Class[] columnTypes = new Class[] {Object.class, Integer.class};
								@SuppressWarnings({ "rawtypes", "unchecked" })
								@Override
								public Class getColumnClass(int columnIndex) {return columnTypes[columnIndex];}});
						TableColumn ColMed = tableAchats.getColumnModel().getColumn(0);
						ColMed.setCellEditor(new DefaultCellEditor(box));
					}
			}
			});
			
			JButton buttonPlus = new JButton("+");
			buttonPlus.setBounds(563, 141, 44, 23);
			contentPane.add(buttonPlus);
			buttonPlus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(chckbxOrdonnance.isSelected())
						((DefaultTableModel)tableAchats.getModel()).addRow(new Object[] {"", 0, 0});
					else ((DefaultTableModel)tableAchats.getModel()).addRow(new Object[] {"", 0});
				}
			});
			
			JButton buttonMinus = new JButton("-");
			buttonMinus.setBounds(563, 187, 44, 23);
			contentPane.add(buttonMinus);
			buttonMinus.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					((DefaultTableModel)tableAchats.getModel()).removeRow(tableAchats.getSelectedRow());
				}
			});
			
			JCheckBox chckbxClientPermanent = new JCheckBox("Client permanent");
			chckbxClientPermanent.setBounds(22, 17, 141, 23);
			contentPane.add(chckbxClientPermanent);
			chckbxClientPermanent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					textFieldSS.setEnabled(chckbxClientPermanent.isSelected());
					lblNumroDeScurit.setEnabled(chckbxClientPermanent.isSelected());
					btnNouveauClient.setEnabled(chckbxClientPermanent.isSelected());
				}
			});
			
			JButton btnConfirmer = new JButton("Confirmer");
			btnConfirmer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ArrayList<MedicamentPrescrit> prescription = new ArrayList<MedicamentPrescrit>();
					if(chckbxOrdonnance.isSelected()) 
						for (int i = 0; i < model.getRowCount(); i++) {
							if((((String)model.getValueAt(i, 0)).equals(""))||(((int)model.getValueAt(i, 1))==0)||(((int)model.getValueAt(i, 2))==0)) {
								tableAchats.setRowSelectionInterval(i, i);
								return;
							}
							//prescription.add(new MedicamentPrescrit((String)model.getValueAt(i, 0),Integer.parseInt((String)model.getValueAt(i, 1)),Integer.parseInt((String)model.getValueAt(i, 2))));
							prescription.add(new MedicamentPrescrit((String)model.getValueAt(i, 0),(int)model.getValueAt(i, 1),(int)model.getValueAt(i, 2)));
						}
					else 
						for (int i = 0; i < model.getRowCount(); i++) {
							if((((String)model.getValueAt(i, 0)).equals(""))||(((String)model.getValueAt(i, 1)).equals(""))) {
								tableAchats.setRowSelectionInterval(i, i);
								return;
							}
							prescription.add(new MedicamentPrescrit((String)model.getValueAt(i, 0),Integer.parseInt((String)model.getValueAt(i, 1)),0));
						}
					if(chckbxClientPermanent.isSelected()) {
						if(ClientPermanent.Existe(Long.parseLong((String)textFieldSS.getText().replaceAll(" ", "")))) {
							ClientPermanent cl = new ClientPermanent(Long.parseLong((String)textFieldSS.getText().replaceAll(" ", "")),f);
							cl.Acheter(chckbxOrdonnance.isSelected(), prescription, Stock, exter, inter, f);
						}
					}
					else {
						Donnees.Acheter(chckbxOrdonnance.isSelected(), prescription, Stock, exter, inter, f);
					}
				}
			});
			btnConfirmer.setBounds(563, 283, 111, 23);
			contentPane.add(btnConfirmer);
			f.setVisible(true);
		}
		
	public static void PopUpAjoutClient() {
			JFrame f = new JFrame();
			f.setAlwaysOnTop(true);
			f.setTitle("Nouveau client");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 450, 260);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel lblNom = new JLabel("Nom:");
			lblNom.setBounds(90, 69, 46, 14);
			contentPane.add(lblNom);
			
			JLabel lblPrenom = new JLabel("Prenom:");
			lblPrenom.setBounds(90, 94, 59, 14);
			contentPane.add(lblPrenom);
			
			JLabel lblge = new JLabel("\u00C2ge:");
			lblge.setBounds(90, 119, 46, 14);
			contentPane.add(lblge);
			
			JLabel lblNumroDeScurit = new JLabel("Num\u00E9ro de s\u00E9curit\u00E9 sociale:");
			lblNumroDeScurit.setBounds(32, 44, 170, 14);
			contentPane.add(lblNumroDeScurit);
			
			JCheckBox chckbxMaladieChronique = new JCheckBox("Maladie chronique");
			chckbxMaladieChronique.setBounds(90, 140, 155, 23);
			contentPane.add(chckbxMaladieChronique);
			
			NumberFormatter SS= new NumberFormatter(NumberFormat.getNumberInstance());
			SS.setMinimum(111111111111111L);
			SS.setMaximum(999999999999999L);
			
			JFormattedTextField textFieldSecS = new JFormattedTextField(SS);
			textFieldSecS.setBounds(212, 41, 170, 20);
			contentPane.add(textFieldSecS);
			textFieldSecS.setColumns(10);
			
			JTextField textFieldNom = new JTextField();
			textFieldNom.setText("");
			textFieldNom.setBounds(159, 66, 145, 20);
			contentPane.add(textFieldNom);
			textFieldNom.setColumns(10);
			
			JTextField textFieldPrenom = new JTextField();
			textFieldPrenom.setBounds(159, 91, 145, 20);
			contentPane.add(textFieldPrenom);
			textFieldPrenom.setColumns(10);
			
			NumberFormatter entier= new NumberFormatter(NumberFormat.getNumberInstance());
			entier.setValueClass(Integer.class);
			entier.setMinimum(0);
			entier.setMaximum(130);
			entier.setAllowsInvalid(false);
			
			JFormattedTextField textFieldAge = new JFormattedTextField(entier);
			textFieldAge.setBounds(159, 116, 55, 20);
			contentPane.add(textFieldAge);
			textFieldAge.setColumns(10);
			
			JButton btnAjouter = new JButton("Ajouter");
			btnAjouter.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(textFieldSecS.getText().equals("")||(textFieldSecS.getText().length()!=19)) {
						textFieldSecS.setText("111 111 111 111 111");
							return;}
					if(textFieldNom.getText().equals("")) {
						textFieldNom.setText("!!!");
						return;}
					if(textFieldPrenom.getText().equals("")) {
						textFieldPrenom.setText("!!!");
						return;}
					if((textFieldAge.getText().equals("")||(Integer.parseInt(textFieldAge.getText())<0)||(Integer.parseInt(textFieldAge.getText())>130))) {
						textFieldAge.setText("0");
						return;}
					ClientPermanent cl = new ClientPermanent(textFieldNom.getText(), textFieldPrenom.getText(),Integer.parseInt(textFieldAge.getText().replaceAll(" ", "")),Long.parseLong(textFieldSecS.getText().replaceAll(" ", "")), chckbxMaladieChronique.isSelected(), new HashMap<String,Integer>());
					cl.AjouterClient(false);
					f.setVisible(false);
					f.dispose();
				}
			});
			btnAjouter.setBounds(159, 180, 89, 23);
			contentPane.add(btnAjouter);
			
			f.setVisible(true);
		}
	//Ce qui suit est pour la gestion des commandes
	public static JFrame ServirCommandeClient(String cli, String nomMed, Paire<Integer, Integer> P) {
			JFrame f = new JFrame();
			f.setTitle("Commande de client servie");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setBounds(100, 100, 450, 201);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JTextArea textField = new JTextArea();
			textField.setEditable(false);
			textField.setText("Client : "+cli+"\nNom du médicament : "+nomMed+"\n La commande à bien été servie.");
			textField.setBounds(38, 28, 349, 74);
			contentPane.add(textField);
			textField.setColumns(10);
			
			JButton btnOk = new JButton("OK");
			btnOk.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					P.valeur=1;
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
				}
			});
			btnOk.setBounds(163, 113, 89, 23);
			contentPane.add(btnOk);
			
			return f;
		}
		
	public static JFrame ServirCommandeClinique(String clin, String code, Paire<Integer, Integer> P) {
			JFrame f = new JFrame();
			f.setTitle("Commande de clinique servie");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setBounds(100, 100, 450, 201);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JTextArea textField = new JTextArea();
			textField.setEditable(false);
			textField.setText("Clinique : "+clin+"\nCode de la commande : "+code+"\n La commande à bien été servie.");
			textField.setBounds(38, 28, 349, 74);
			contentPane.add(textField);
			textField.setColumns(10);
			
			JButton btnOk = new JButton("OK");
			btnOk.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					P.valeur=1;
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
				}
			});
			btnOk.setBounds(163, 113, 89, 23);
			contentPane.add(btnOk);
			
			return f;
		}
		
	public static void AnnulerCommande(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter, JFrame f/*Paire<Integer, Integer> pr*/) {
			JFrame k = new JFrame();
			k.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			k.setBounds(100, 100, 450, 250);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			k.setContentPane(contentPane);
			contentPane.setLayout(null);
			k.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windEv) {
					f.setEnabled(true);
					windEv.getWindow().setVisible(false);
					windEv.getWindow().dispose();
				}
			});
			
			JLabel lbl1 = new JLabel("Num\u00E9ro de s\u00E9curit\u00E9 sociale :");
			lbl1.setBounds(40, 85, 160, 14);
			contentPane.add(lbl1);
			
			JLabel lbl2 = new JLabel("M\u00E9dicament command\u00E9 :");
			lbl2.setBounds(40, 60, 160, 14);
			contentPane.add(lbl2);
			
			JTextField textField = new JTextField();
			textField.setBounds(221, 57, 151, 20);
			contentPane.add(textField);
			textField.setColumns(10);
			
			JTextField textField_1 = new JTextField();
			textField_1.setBounds(221, 82, 151, 20);
			contentPane.add(textField_1);
			textField_1.setColumns(10);
			
			JLabel lblNomMdecin = new JLabel("Nom m\u00E9decin :");
			lblNomMdecin.setBounds(40, 110, 160, 14);
			contentPane.add(lblNomMdecin);
			lblNomMdecin.setEnabled(false);
			
			JTextField textField_2 = new JTextField();
			textField_2.setBounds(221, 107, 151, 20);
			contentPane.add(textField_2);
			textField_2.setColumns(10);
			textField_2.setEnabled(false);
			
			JCheckBox chckbxClientPermanent = new JCheckBox("Client permanent");
			chckbxClientPermanent.setSelected(true);
			chckbxClientPermanent.setBounds(40, 17, 130, 23);
			contentPane.add(chckbxClientPermanent);
			
			JCheckBox chckbxMdecinConventionn = new JCheckBox("Médecin conventionné :");
			chckbxMdecinConventionn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					chckbxClientPermanent.setSelected(!chckbxMdecinConventionn.isSelected());
					if(chckbxMdecinConventionn.isSelected()) {
					lbl1.setText("Code de la commande :");
					lbl2.setText("Nom clinique :");
					}
					if(chckbxClientPermanent.isSelected()) {
						lbl1.setText("Numéro de sécurité sociale :");
						lbl2.setText("Médicament commandé :");
						}
					textField_2.setEnabled(chckbxMdecinConventionn.isSelected());
					lblNomMdecin.setEnabled(chckbxMdecinConventionn.isSelected());
				}
			});
			chckbxMdecinConventionn.setBounds(179, 17, 151, 23);
			contentPane.add(chckbxMdecinConventionn);
			
			chckbxClientPermanent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					chckbxMdecinConventionn.setSelected(!chckbxClientPermanent.isSelected());
					if(chckbxClientPermanent.isSelected()) {
					lbl1.setText("Numéro sécurité sociale :");
					lbl2.setText("Médicament commandé :");
					}
					if(chckbxMdecinConventionn.isSelected()) {
						lbl1.setText("Code de la commande :");
						lbl2.setText("Nom clinique :");
						}
					textField_2.setEnabled(!chckbxClientPermanent.isSelected());
					lblNomMdecin.setEnabled(!chckbxClientPermanent.isSelected());
				}
			});
			
			JButton btnAnnuler = new JButton("Annuler");
			btnAnnuler.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(chckbxClientPermanent.isSelected()) {
						long SS = 0;
						try {
							SS = Long.parseLong(textField_1.getText());
						}
						catch(NumberFormatException ex) {
							textField_1.setBackground(Color.RED);
							PopUpAnnulerCommande(textField_1.getText(), false, "Numéro de sécurité sociale non valable", new ArrayList<MedicamentPrescrit>());
							return ;
						}
						if(ClientPermanent.Existe(SS)) {
						ClientPermanent cl = new ClientPermanent(SS, f);
						cl.AnnulerCommande(textField_1.getText(), Stock, inter, exter);
						}
						else {
							textField_1.setBackground(Color.RED);
							PopUpAnnulerCommande(textField_1.getText(), false, "Aucun client enregistré sous ce numéro", new ArrayList<MedicamentPrescrit>());
							return ;
						}
					}else {
						MedecinConventionne med = new MedecinConventionne(textField_1.getText(), textField_2.getText());
						med.AnnulerCommande(textField.getText(), Stock, exter, inter, f);
					}
					k.setEnabled(true);
				}
			});
			btnAnnuler.setBounds(163, 168, 89, 23);
			contentPane.add(btnAnnuler);
			
			k.setVisible(true);
		}

	public static void PopUpAnnulerCommande(String ID, boolean Success, String RaisonEchec, ArrayList<MedicamentPrescrit> presc) {
			JFrame f = new JFrame();
			f.setTitle("Titre");
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 450, 285);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel labelSuccessEchec = new JLabel("");
			labelSuccessEchec.setBounds(26, 11, 290, 14);
			contentPane.add(labelSuccessEchec);
			
			JLabel lblMotif = new JLabel("Motif :");
			lblMotif.setBounds(26, 36, 46, 14);
			contentPane.add(lblMotif);
			
			JLabel labelMOTIF = new JLabel("");
			labelMOTIF.setBounds(82, 36, 290, 14);
			contentPane.add(labelMOTIF);
			
			JLabel lblId = new JLabel("ID :");
			lblId.setBounds(26, 61, 46, 14);
			contentPane.add(lblId);
			
			JLabel labelID = new JLabel("");
			labelID.setBounds(82, 61, 220, 14);
			contentPane.add(labelID);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(82, 104, 290, 130);
			contentPane.add(scrollPane);
			
			JTable table = new JTable();
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"M\u00E9dicament", "Quantit\u00E9"
				}
			));
			table.setRowSelectionAllowed(false);
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			table.setDefaultEditor(Object.class, null);
			scrollPane.setViewportView(table);
			for(int i=0;i<presc.size();i++) 
				model.addRow(new Object[] {presc.get(i).nom,presc.get(i).quantité});
			labelID.setText(ID);
			labelMOTIF.setText(RaisonEchec);
			if(Success) {
				lblMotif.setEnabled(false);
				f.setTitle("Commande annulée avec succès");
				labelSuccessEchec.setText("La commande à été annulée avec succès !");
			}else {
				f.setTitle("La commande n'a pas pu être annulée");
				labelSuccessEchec.setText("La commande n'a pas pu être annulée !");
			}
			f.setVisible(true);
		}
	
	public static void RetirerCommande(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter, JFrame k) {
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setBounds(100, 100, 450, 250);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windEv) {
					k.setEnabled(true);
					windEv.getWindow().setVisible(false);
					windEv.getWindow().dispose();
				}
			});
			
			JLabel lbl1 = new JLabel("Num\u00E9ro de s\u00E9curit\u00E9 sociale :");
			lbl1.setBounds(40, 85, 160, 14);
			contentPane.add(lbl1);
			
			JLabel lbl2 = new JLabel("M\u00E9dicament command\u00E9 :");
			lbl2.setBounds(40, 60, 160, 14);
			contentPane.add(lbl2);
			
			JTextField textField = new JTextField();
			textField.setBounds(221, 57, 151, 20);
			contentPane.add(textField);
			textField.setColumns(10);
			
			JTextField textField_1 = new JTextField();
			textField_1.setBounds(221, 82, 151, 20);
			contentPane.add(textField_1);
			textField_1.setColumns(10);
			
			JLabel lblNomMdecin = new JLabel("Nom m\u00E9decin :");
			lblNomMdecin.setBounds(40, 110, 160, 14);
			contentPane.add(lblNomMdecin);
			lblNomMdecin.setEnabled(false);
			
			JTextField textField_2 = new JTextField();
			textField_2.setBounds(221, 107, 151, 20);
			contentPane.add(textField_2);
			textField_2.setColumns(10);
			textField_2.setEnabled(false);
			
			JCheckBox chckbxClientPermanent = new JCheckBox("Client permanent");
			chckbxClientPermanent.setSelected(true);
			chckbxClientPermanent.setBounds(40, 17, 130, 23);
			contentPane.add(chckbxClientPermanent);
			
			JCheckBox chckbxMdecinConventionn = new JCheckBox("Médecin conventionné :");
			chckbxMdecinConventionn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					chckbxClientPermanent.setSelected(!chckbxMdecinConventionn.isSelected());
					if(chckbxMdecinConventionn.isSelected()) {
					lbl1.setText("Code de la commande :");
					lbl2.setText("Nom clinique :");
					}
					if(chckbxClientPermanent.isSelected()) {
						lbl1.setText("Numéro de sécurité sociale :");
						lbl2.setText("Médicament commandé :");
						}
					textField_2.setEnabled(chckbxMdecinConventionn.isSelected());
					lblNomMdecin.setEnabled(chckbxMdecinConventionn.isSelected());
				}
			});
			chckbxMdecinConventionn.setBounds(179, 17, 151, 23);
			contentPane.add(chckbxMdecinConventionn);
			
			chckbxClientPermanent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					chckbxMdecinConventionn.setSelected(!chckbxClientPermanent.isSelected());
					if(chckbxClientPermanent.isSelected()) {
					lbl1.setText("Numéro sécurité sociale :");
					lbl2.setText("Médicament commandé :");
					}
					if(chckbxMdecinConventionn.isSelected()) {
						lbl1.setText("Code de la commande :");
						lbl2.setText("Nom clinique :");
						}
					textField_2.setEnabled(!chckbxClientPermanent.isSelected());
					lblNomMdecin.setEnabled(!chckbxClientPermanent.isSelected());
				}
			});
			
			JButton btnRetirer = new JButton("Retirer");
			btnRetirer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(chckbxClientPermanent.isSelected()) {
						long SS = 0;
						try {
							SS = Long.parseLong(textField_1.getText());
						}
						catch(NumberFormatException ex) {
							textField_1.setBackground(Color.RED);
							JOptionPane.showMessageDialog(f, "On ne peut pas retirer la commande! \r Erreur numéro de sécurité social erroné");
							return ;
						}
						if(ClientPermanent.Existe(SS)) {
						ClientPermanent cl = new ClientPermanent(SS, f);
						cl.RetirerCommande(textField_1.getText(), f);
						}
						else {
							textField_1.setBackground(Color.RED);
							JOptionPane.showMessageDialog(f, "On ne peut pas retirer la commande! Le client n'existe pas !");
							return ;
						}
					}else {
						MedecinConventionne med = new MedecinConventionne(textField_1.getText(), textField_2.getText());
						med.RetireCommande(textField.getText(), f);
					}
				k.setEnabled(true);	
				}
			});
			btnRetirer.setBounds(163, 168, 89, 23);
			contentPane.add(btnRetirer);
			
			f.setVisible(true);
		}

	public static void GestionCommandes(HashMap<String, ArrayList<MedicamentEnStock>> Stock,HashMap<String,MedicamentProduitEnExterne> exter,HashMap<String,MedicamentEnInterne> inter) {
			JFrame f = new JFrame();
			f.setResizable(false);
			f.setTitle("Gestion des commandes");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBounds(100, 100, 600, 330);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			f.setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JButton btnServirLesCommandes = new JButton("Servir les commandes de clients");
			btnServirLesCommandes.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					ArrayList<Paire<JFrame, Paire<Integer, Integer>>> list = new ArrayList<Paire<JFrame, Paire<Integer, Integer>>>();
					list = ClientPermanent.ServirCommandesClients(Stock, exter, inter);
					/*for(int i =0;i<list.size();i++) {
						list.get(i).valeur.setVisible(true);
						list.get(i).valeur2.valeur=0;
						while(list.get(i).valeur2.valeur==0) {
							
						}
					}*/
					JOptionPane.showMessageDialog(f, list.size()+" Commandes servies");
				}
			});
			btnServirLesCommandes.setBounds(132, 44, 300, 23);
			contentPane.add(btnServirLesCommandes);
			
			JButton btnServirLesCommandes_1 = new JButton("Servir les commandes de m\u00E9decins");
			btnServirLesCommandes_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					ArrayList<Paire<JFrame, Paire<Integer, Integer>>> list = new ArrayList<Paire<JFrame, Paire<Integer, Integer>>>();
					list = MedecinConventionne.ServirCommandes(Stock, exter, inter);
					
					/*for(int i =0;i<list.size();i++) {
						list.get(i).valeur.setVisible(true);
					}*/
					JOptionPane.showMessageDialog(f, list.size()+" Commandes servies");
				}
			});
			btnServirLesCommandes_1.setBounds(132, 95, 300, 23);
			contentPane.add(btnServirLesCommandes_1);
			
			JButton btnAnnulerUneCommande = new JButton("Annuler une commande");
			btnAnnulerUneCommande.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					f.setEnabled(false);
					AnnulerCommande(Stock, exter, inter, f);
					f.setEnabled(true);
				}
			});
			btnAnnulerUneCommande.setBounds(181, 145, 200, 23);
			contentPane.add(btnAnnulerUneCommande);
			
			JButton btnRetirerUneCommande = new JButton("Retirer une commande");
			btnRetirerUneCommande.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					f.setEnabled(false);
					RetirerCommande(Stock, exter, inter, f);
				}
			});
			btnRetirerUneCommande.setBounds(181, 200, 200, 23);
			contentPane.add(btnRetirerUneCommande);
			f.setVisible(true);
		}

	
}