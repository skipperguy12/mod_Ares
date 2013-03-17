package net.minecraft.src;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as GoldBattle is given credit
//You may not claim this to be your own
//You may not remove these comments

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ServerData;


public class Ares_ServerGUI extends GuiScreen {
	private Minecraft mc = Minecraft.getMinecraft();
	private ArrayList<Ares_ThreadPollServers> serverList;
	private int menuButtons = 4;
	private int serverListSpace = 10;
	public static boolean inGame;

	/**
	 * Default constructor
	 */
	public Ares_ServerGUI(boolean inGame) {
		//background boolean
		this.inGame = inGame;
		// Construct the server polls here.
		serverList = new ArrayList<Ares_ThreadPollServers>();
		// add all the servers
		serverList.add(new Ares_ThreadPollServers("alpha.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("beta.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("gamma.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("delta.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("epsilon.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("zeta.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("eta.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("theta.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("iota.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("kappa.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("lambda.oc.tc", 25565));
		serverList.add(new Ares_ThreadPollServers("nostalgia.oc.tc", 25565));
		// update all their info
		runServerPolls();
	}

	/**
	 * This is init of the gui when it is about to get drawn. You should only
	 * have buttons/control elements in here.
	 */
	public void initGui() {
		// clear the list
		this.buttonList.clear();
		// top row buttons
		int widthDis = 5;
		this.buttonList.add(new GuiButton(0, this.width - (widthDis+=75), 5, 75,
				20, "Cancel"));
		//only be able to go to multiplayer menu when you not ingame
		if(!inGame)
			this.buttonList.add(new GuiButton(1, this.width - (widthDis+=78), 5,
				75, 20, "Old Menu"));
		this.buttonList.add(new GuiButton(2, this.width - (widthDis+=43), 5,
				40, 20, "Stats"));
		this.buttonList.add(new GuiButton(3, this.width - (widthDis+=78), 5,
				75, 20, "Refresh"));



		
		//this is a copy of the server list drawing.
		//the buttons need to be drawn here because they only need to be drawn once.
		//make sure this matches the draw method
		int height = 25;
		int width = 40;
		for (int server=0; server<serverList.size();server++) {
			if(height+(3*fontRenderer.FONT_HEIGHT+serverListSpace)>this.height){
				width+=200;
				height = 25;
			}
			height += 2*fontRenderer.FONT_HEIGHT+serverListSpace;
			this.buttonList.add(new GuiButton(server+menuButtons, width-35, height-5, 30,20, "Play"));
			height += fontRenderer.FONT_HEIGHT;
		}
	}

	/**
	 * This method is a override method for drawing a gui All "painting" should
	 * take place in here
	 */
	public void drawScreen(int x, int y, float f) {
		if(!inGame)
			drawDefaultBackground();
		else
			drawGradientRect(0, 0, this.width,this.height, -1073741824,-1073741824);
		drawGradientRect(5, 3, this.width - 5, 20 + 6, 1615855616, -1602211792);
		// title
		drawString(this.fontRenderer, "Project Ares Servers", 10, 10, 16777215);

		// server info drawing
		int height = 25;
		int width = 40;
		for (int server=0; server<serverList.size();server++) {
			if(height+(3*fontRenderer.FONT_HEIGHT+serverListSpace)>this.height){
				width+=200;
				height = 25;
			}
			if(serverList.get(server).serverIP!=null)
				drawString(this.fontRenderer, "\u00A74" + serverList.get(server).serverIP,width, height += fontRenderer.FONT_HEIGHT+serverListSpace, 16777215);
			if(serverList.get(server).MOTD!=null)
				drawString(this.fontRenderer, serverList.get(server).MOTD, width,height += fontRenderer.FONT_HEIGHT, 16777215);
			//would have added button here
			if(serverList.get(server).populationInfo!=null || serverList.get(server).pingToServer!=null)
				drawString(this.fontRenderer,"\u00A7f" + serverList.get(server).populationInfo + "  \u00A77"+ serverList.get(server).pingToServer, width,height += fontRenderer.FONT_HEIGHT, 16777215);

		}
		// tells the super class to paint everything
		super.drawScreen(x, y, f);
	}

	/**
	 * If a button is clicked this method gets called. The id is the number
	 * given to the button during init.
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			// cancel
			if (par1GuiButton.id == 0) {
				if(!inGame)
					this.mc.displayGuiScreen(new GuiMainMenu());
				else
					this.mc.setIngameFocus();
			}
			// old menu
			else if (par1GuiButton.id == 1) {
				if(!inGame){
					AresGuiListener.toggleMultiGUI(false);
					this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
				}
			}
			// refresh
			else if (par1GuiButton.id == 2) {
				String username = this.mc.session.username;
				try {
					Desktop.getDesktop().browse(new URI( "http://"+mod_Ares.serverDomain+"/"+username));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else if (par1GuiButton.id == 3) {
				runServerPolls();
			}
			else if(par1GuiButton.id <= serverList.size()-1+menuButtons){
				String serverip = serverList.get(par1GuiButton.id-menuButtons).serverIP;
				String serverport = Integer.toString(serverList.get(par1GuiButton.id-menuButtons).port);
				ServerData joinServer = new ServerData(serverip,serverip+":"+serverport);
				//connect
				mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
			}
		}
	}

	/**
	 * Go through all the servers and poll for new info
	 */
	private void runServerPolls() {
		for (final Ares_ThreadPollServers temp : serverList) {
			Thread thread = new Thread() {
				public void run() {
					temp.run();
				}
			};
			thread.start();
		}
	}
}
