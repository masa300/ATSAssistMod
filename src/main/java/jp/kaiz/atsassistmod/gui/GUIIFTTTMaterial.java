package jp.kaiz.atsassistmod.gui;

import jp.kaiz.atsassistmod.ATSAssistCore;
import jp.kaiz.atsassistmod.block.tileentity.TileEntityIFTTT;
import jp.kaiz.atsassistmod.gui.parts.*;
import jp.kaiz.atsassistmod.ifttt.IFTTTContainer;
import jp.kaiz.atsassistmod.ifttt.IFTTTContainer.This.Minecraft.RedStoneInput.ModeType;
import jp.kaiz.atsassistmod.ifttt.IFTTTContainer.This.RTM.SimpleDetectTrain.DetectMode;
import jp.kaiz.atsassistmod.ifttt.IFTTTType;
import jp.kaiz.atsassistmod.network.PacketIFTTT;
import jp.kaiz.atsassistmod.utils.KaizUtils;
import jp.ngt.rtm.modelpack.state.DataType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.commons.lang3.SerializationUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIIFTTTMaterial extends GuiScreen {
	private final List<GuiTextField> textFieldList = new ArrayList<>();
	private final TileEntityIFTTT tile;
	private IFTTTType.IFTTTEnumBase type = null;
	private IFTTTType.IFTTTEnumBase oldType = null;
	private IFTTTContainer ifcb = null;
	private int ifcbIndex = -1;

	public GUIIFTTTMaterial(TileEntityIFTTT tile) {
		this.tile = tile;
	}

	//文字の描画
	//横はthis.width
	//縦はthis.height
	//this.fontRendererObj.drawString("ここに文字", 横座標, 縦座標, 白なら0xffffff);
	@Override
	public void drawScreen(int mouseX, int mouseZ, float partialTick) {
		super.drawDefaultBackground();

		GL11.glPushMatrix();
		GL11.glTranslatef(1.0F, 1.0F, 1.0F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		int tw = 750 / 2, th = 422 / 2;

		if (this.type == null) {
			GL11.glPushMatrix();
			GL11.glTranslatef((this.width - tw) / 2f, (this.height - th) / 2f, 1.0F);
			GL11.glScalef(tw / 256f, tw / 256f, 1.0F);
			this.mc.getTextureManager().bindTexture(GuiTextureManager.IFTTTBaseLayer.texture);
			this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GL11.glPopMatrix();
		} else {
			switch (this.type.getId()) {
				case 100://IFTTTType.This.Select
//					GL11.glPushMatrix();
//					GL11.glTranslatef((this.width - tw) / 2f, (this.height - th) / 2f, 1.0F);
//					GL11.glScalef(tw / 256f, tw / 256f, 1.0F);
//					this.mc.getTextureManager().bindTexture(GuiTextureManager.ThisBaseLayer.texture);
//					this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
//					GL11.glPopMatrix();
					this.fontRendererObj.drawString("IFTTT : This : 選択",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("Minecraft",
							this.width / 2 - 170, this.height / 2 - 90, 0xffffff);
					this.fontRendererObj.drawString("RTM",
							this.width / 2 - 170, this.height / 2 - 25, 0xffffff);
					this.fontRendererObj.drawString("ATSAssist",
							this.width / 2 - 170, this.height / 2 + 40, 0xffffff);
					break;
				case 110://RedStoneInput
					this.fontRendererObj.drawString("IFTTT : This : RedStoneInput",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("Input",
							this.width / 2 - 50, this.height / 2 - 25, 0xffffff);
					ModeType modeType = ((IFTTTContainer.This.Minecraft.RedStoneInput) this.ifcb).getMode();
					for (Object o : this.buttonList) {
						GuiButton button = (GuiButton) o;
						if (button.id == 1000) {
							button.displayString = modeType.name;
						}
					}
					for (GuiTextField textField : this.textFieldList) {
						textField.setVisible(modeType.needStr);
					}
					break;
				case 120://単純列検
					this.fontRendererObj.drawString("IFTTT : This : 単純列車検知",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("動作モード",
							this.width / 2 - 75, this.height / 2 - 25, 0xffffff);
					DetectMode detectMode = ((IFTTTContainer.This.RTM.SimpleDetectTrain) this.ifcb).getDetectMode();
					for (Object o : this.buttonList) {
						GuiButton button = (GuiButton) o;
						if (button.id == 1000) {
							button.displayString = detectMode.name;
						}
					}
					break;
				case 200://IFTTTType.That.Select
					this.fontRendererObj.drawString("IFTTT : That : 選択",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("Minecraft",
							this.width / 2 - 170, this.height / 2 - 90, 0xffffff);
					this.fontRendererObj.drawString("RTM",
							this.width / 2 - 170, this.height / 2 - 25, 0xffffff);
					this.fontRendererObj.drawString("ATSAssist",
							this.width / 2 - 170, this.height / 2 + 40, 0xffffff);
					break;
				case 210://DataMap
					this.fontRendererObj.drawString("IFTTT : That : RedStoneOutput",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("両数出力",
							this.width / 2 - 50, this.height / 2 - 50, 0xffffff);
					this.fontRendererObj.drawString("出力レベル",
							this.width / 2 - 50, this.height / 2 - 25, 0xffffff);

					for (Object o : this.buttonList) {
						GuiButton button = (GuiButton) o;
						if (button.id == 1000) {
							button.displayString = ((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).isTrainCarsOutput() ? "有効" : "無効";
						}
					}
					for (GuiTextField textField : this.textFieldList) {
						textField.setVisible(!((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).isTrainCarsOutput());
					}
					break;
				case 221://DataMap
					this.fontRendererObj.drawString("IFTTT : That : DataMap",
							this.width / 4, 20, 0xffffff);
					this.fontRendererObj.drawString("DataType",
							this.width / 2 - 50, this.height / 2 - 50, 0xffffff);
					this.fontRendererObj.drawString("Key",
							this.width / 2 - 50, this.height / 2 - 25, 0xffffff);
					this.fontRendererObj.drawString("Value",
							this.width / 2 - 50, this.height / 2, 0xffffff);

					for (Object o : this.buttonList) {
						GuiButton button = (GuiButton) o;
						if (button.id == 1000) {
							button.displayString = ((IFTTTContainer.That.RTM.DataMap) this.ifcb).getDataType().key;
						}
					}
					break;
			}
		}
		super.drawScreen(mouseX, mouseZ, partialTick);
		this.textFieldList.forEach(GuiTextField::drawTextBox);
		GL11.glPopMatrix();
	}

	private void addDetail(int baseButtonID, int widthBase, int heightBase, int number, IFTTTContainer ifcb, int size) {
//        addAddButton(baseButtonID, widthBase, heightBase, number, ifcb.getType().getName() + " (" + ifcb.getExplanation() + ")");
//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glTranslatef(widthBase - 1, heightBase - 1, 1.0F);
//        GL11.glScalef(45 / 122F, 45 / 122F, 1.0F);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        this.mc.getTextureManager().bindTexture(GuiTextureManager.IFTTTContainer.location);
//        int tw = 256 / 2, th = 133 / 2;
//        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glPopMatrix();
		if (number >= 6) {
			return;
		}
		widthBase += number < 3 ? 95 * number : 95 * (number - 3);
		heightBase += number < 3 ? size < 3 ? 25 : 0 : 50;

		this.buttonList.add(
				new GuiDummyButtonIFTTTContainer(widthBase, heightBase, ifcb));
		this.buttonList.add(
				new GuiButtonEdit(baseButtonID + number, widthBase + 41, heightBase + 34));
		this.buttonList.add(
				new GuiButtonDelete(baseButtonID + number, widthBase + 65, heightBase + 34));
	}

	private void addAddButton(int baseButtonID, int widthBase, int heightBase, int number, int size) {
		if (number >= 6) {
			return;
		}
		widthBase += 95 * (number < 3 ? number : (number - 3));
		heightBase += number < 3 ? size < 3 ? 25 : 0 : 50;

		this.buttonList.add(
				new GuiButtonAdd(baseButtonID + number, widthBase, heightBase));
	}

	//チェックボックスも
	//ボタンはここ
	//this.buttonList.add(new GuiButton(id,横座標,縦座標,横長さ,縦長さ,文字列))
	@Override
	public void initGui() {
		super.initGui();
		super.buttonList.clear();
		this.textFieldList.clear();

		this.oldType = this.type;

		this.buttonList.add(new GuiButtonExit(90, this.width / 2 + 175, this.height / 2 - 101));
		if (this.type == null) {
			List<IFTTTContainer> thisList = this.tile.getThisList();
			int i0 = 0;
			int thisSize = thisList.size();
			if (!thisList.isEmpty()) {
				for (i0 = 0; i0 < thisList.size(); i0++) {
					this.addDetail(100, this.width / 2 - 110, this.height / 2 - 100, i0, thisList.get(i0), thisSize);
				}
			}
			this.addAddButton(100, this.width / 2 - 73, this.height / 2 - 86, i0, thisSize);

			List<IFTTTContainer> thatList = this.tile.getThatList();
			int i1 = 0;
			int thatSize = thatList.size();
			if (!thatList.isEmpty()) {
				for (i1 = 0; i1 < thatList.size(); i1++) {
					this.addDetail(200, this.width / 2 - 110, this.height / 2 + 5, i1, thatList.get(i1), thatSize);
				}
			}
			this.addAddButton(200, this.width / 2 - 73, this.height / 2 + 19, i1, thatSize);
		} else {
			int id = this.type.getId();
			if (id == 100) {//IFTTTType.This.Select
				this.addSelectButton(IFTTTType.This.Minecraft.values(), this.width / 2 - 170, this.height / 2 - 75);
				this.addSelectButton(IFTTTType.This.RTM.values(), this.width / 2 - 170, this.height / 2 - 10);
				this.addSelectButton(IFTTTType.This.ATSAssist.values(), this.width / 2 - 170, this.height / 2 - 55);
				this.buttonList.add(new GuiButton(990, this.width / 2 - 50, this.height - 25, 100, 20, "戻る"));
			} else if (id == 110) {//RedStoneInput
				this.buttonList.add(new GuiButton(1000, this.width / 2 - 15, this.height / 2 - 30, 30, 20, ""));
				this.addGuiTextField(String.valueOf(((IFTTTContainer.This.Minecraft.RedStoneInput) this.ifcb).getValue()), this.width / 2 + 30, this.height / 2 - 30, 2, 30);
				this.addDownCommon();
			} else if (id == 120) {//単純列検
				this.buttonList.add(new GuiButton(1000, this.width / 2 + 30, this.height / 2 - 30, 60, 20, ""));
				this.addDownCommon();
			} else if (id == 200) {//IFTTTType.This.Select
				this.addSelectButton(IFTTTType.That.Minecraft.values(), this.width / 2 - 170, this.height / 2 - 75);
				this.addSelectButton(IFTTTType.That.RTM.values(), this.width / 2 - 170, this.height / 2 - 10);
				this.addSelectButton(IFTTTType.That.ATSAssist.values(), this.width / 2 - 170, this.height / 2 - 55);
				this.buttonList.add(new GuiButton(990, this.width / 2 - 50, this.height - 25, 100, 20, "戻る"));
			} else if (id == 210) {//RedStoneOutput
				this.buttonList.add(new GuiButton(1000, this.width / 2 + 30, this.height / 2 - 55, 30, 20, ""));
				this.addGuiTextField(String.valueOf(((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).getOutputLevel()), this.width / 2 + 30, this.height / 2 - 30, Byte.MAX_VALUE, 50);
				this.addDownCommon();
			} else if (id == 221) {//DataMap
				this.buttonList.add(new GuiButton(1000, this.width / 2 + 30, this.height / 2 - 55, 30, 20, ""));
				this.addGuiTextField(String.valueOf(((IFTTTContainer.That.RTM.DataMap) this.ifcb).getKey()), this.width / 2 + 30, this.height / 2 - 30, Byte.MAX_VALUE, 50);
				this.addGuiTextField(String.valueOf(((IFTTTContainer.That.RTM.DataMap) this.ifcb).getValue()), this.width / 2 + 30, this.height / 2 - 5, Byte.MAX_VALUE, 50);
				this.addDownCommon();
			}
		}
	}

	private void addDownCommon() {
		this.buttonList.add(new GuiButton(91, this.width / 2 - 110, this.height - 30, 100, 20, this.ifcbIndex == -1 ? "追加" : "変更"));
		this.buttonList.add(new GuiButton(990, this.width / 2 + 10, this.height - 30, 100, 20, "戻る"));
	}

	private void addSelectButton(IFTTTType.IFTTTEnumBase[] e, int baseWidth, int baseHeight) {
		List<IFTTTType.IFTTTEnumBase> eList = Arrays.asList(e);
		for (int i = 0; i < e.length; i++) {
			GuiButton guiButton = null;
			if (i < 3) {
				guiButton = new GuiButton(eList.get(i).getId(), baseWidth + 120 * i, baseHeight, 100, 20, eList.get(i).getName());
			} else if (i < 6) {
				guiButton = new GuiButton(eList.get(i).getId(), baseWidth + 120 * (i - 3), baseHeight + 25, 100, 20, eList.get(i).getName());
			}
			this.buttonList.add(guiButton);
		}
	}

	private void addGuiTextField(String str, int xPosition, int yPosition, int maxLength, int width) {
		GuiTextField text = new GuiTextField(this.fontRendererObj, xPosition, yPosition, width, 20);
		text.setFocused(false);
		text.setMaxStringLength(maxLength);
		text.setText(str);
		this.textFieldList.add(text);
	}

	private int getIntGuiTextFieldText(int number) {
		String str = this.textFieldList.get(number).getText();
		int i = 0;
		if (str == null || str.equals("")) {
			return i;
		}

		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return i;
		}
		return i;
	}

	private String getStringGuiTextFieldText(int number) {
		String str = this.textFieldList.get(number).getText();
		return str == null || str.equals("") ? "" : str;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.textFieldList.forEach(GuiTextField::updateCursorCounter);
	}

	@Override
	public void mouseClicked(int x, int y, int btn) {
		super.mouseClicked(x, y, btn);
		this.textFieldList.forEach(guiTextField -> guiTextField.mouseClicked(x, y, btn));
		if (this.oldType != this.type) {
			this.initGui();
		}
	}

	@Override
	public void keyTyped(char par1, int par2) {
		if (par2 == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
			return;
		}

//        if ((par2 >= Keyboard.KEY_1 && par2 <= Keyboard.KEY_0) ||
//                (par2 >= Keyboard.KEY_NUMPAD7 && par2 <= Keyboard.KEY_NUMPAD9) ||
//                (par2 >= Keyboard.KEY_NUMPAD4 && par2 <= Keyboard.KEY_NUMPAD6) ||
//                (par2 >= Keyboard.KEY_NUMPAD1 && par2 <= Keyboard.KEY_NUMPAD3) ||
//                par2 == Keyboard.KEY_NUMPAD0 ||
//                par2 == Keyboard.KEY_LEFT ||
//                par2 == Keyboard.KEY_RIGHT ||
//                par2 == Keyboard.KEY_BACK ||
//                par2 == Keyboard.KEY_DELETE) {
		this.textFieldList.forEach(textField -> textField.textboxKeyTyped(par1, par2));
//        }

//        if (par2 == Keyboard.KEY_PERIOD ||
//                par2 == Keyboard.KEY_DECIMAL) {
//            for (GuiTextField textField : this.textFieldList) {
//                if (textField.getMaxStringLength() == 5) {
//                    textField.textboxKeyTyped(par1, par2);
//                }
//            }
//        }
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id >= 100 && button.id < 106) {
			if (this.tile.getThisList().size() > button.id - 100) {
				if (button instanceof GuiButtonDelete) {
					ATSAssistCore.NETWORK_WRAPPER.sendToServer(new PacketIFTTT(this.tile, this.tile.getThisList().get(button.id - 100), button.id - 100, 2));
					this.tile.getThisList().remove(button.id - 100);
					this.initGui();
					return;
				} else {
					this.type = (this.ifcb = SerializationUtils.clone(this.tile.getThisList().get(button.id - 100))).getType();
				}
			} else {
				this.type = IFTTTType.This.Select;
			}
			this.ifcbIndex = button.id - 100;
			return;
		} else if (button.id >= 200 && button.id < 206) {
			if (this.tile.getThatList().size() > button.id - 200) {
				if (button instanceof GuiButtonDelete) {
					ATSAssistCore.NETWORK_WRAPPER.sendToServer(new PacketIFTTT(this.tile, this.tile.getThatList().get(button.id - 200), button.id - 200, 2));
					this.tile.getThatList().remove(button.id - 200);
					this.initGui();
					return;
				} else {
					this.type = (this.ifcb = SerializationUtils.clone(this.tile.getThatList().get(button.id - 200))).getType();
				}
			} else {
				this.type = IFTTTType.That.Select;
			}
			this.ifcbIndex = button.id - 200;
			return;
		} else if (button.id == 110) {
			this.type = (this.ifcb = new IFTTTContainer.This.Minecraft.RedStoneInput()).getType();
			this.ifcbIndex = -1;
			return;
		} else if (button.id == 120) {
			this.type = (this.ifcb = new IFTTTContainer.This.RTM.SimpleDetectTrain()).getType();
			this.ifcbIndex = -1;
			return;
		} else if (button.id == 210) {
			this.type = (this.ifcb = new IFTTTContainer.That.Minecraft.RedStoneOutput()).getType();
			this.ifcbIndex = -1;
			return;
		} else if (button.id == 221) {
			this.type = (this.ifcb = new IFTTTContainer.That.RTM.DataMap()).getType();
			this.ifcbIndex = -1;
			return;
		}

		switch (button.id) {
			case 990://MainMenu
				this.type = IFTTTType.getType(button.id);
				return;
			case 90:
				this.mc.displayGuiScreen(null);
				return;
			case 91:
				switch (this.type.getId()) {
					case 110:
						((IFTTTContainer.This.Minecraft.RedStoneInput) this.ifcb).setValue(this.getIntGuiTextFieldText(0));
						break;
					case 120:
						break;
					case 210:
						((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).setOutputLevel(this.getIntGuiTextFieldText(0));
						break;
					case 221:
						((IFTTTContainer.That.RTM.DataMap) this.ifcb).setKey(this.getStringGuiTextFieldText(0));
						((IFTTTContainer.That.RTM.DataMap) this.ifcb).setValue(this.getStringGuiTextFieldText(1));
						break;
					default:
						return;
				}
				ATSAssistCore.NETWORK_WRAPPER.sendToServer(new PacketIFTTT(this.tile, this.ifcb, this.ifcbIndex, 0));
				this.mc.displayGuiScreen(null);
				return;
		}

		if (this.type != null) {
			switch (this.type.getId()) {
				case 110:
					switch (button.id) {
						case 1000:
							ModeType modeType = ((IFTTTContainer.This.Minecraft.RedStoneInput) this.ifcb).getMode();
							((IFTTTContainer.This.Minecraft.RedStoneInput) this.ifcb).setMode((ModeType) KaizUtils.getNextEnum(modeType));
							break;
					}
					break;
				case 120:
					switch (button.id) {
						case 1000:
							DetectMode modeType = ((IFTTTContainer.This.RTM.SimpleDetectTrain) this.ifcb).getDetectMode();
							((IFTTTContainer.This.RTM.SimpleDetectTrain) this.ifcb).setDetectMode((DetectMode) KaizUtils.getNextEnum(modeType));
							break;
					}
					break;
				case 210:
					switch (button.id) {
						case 1000:
							((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).
									setTrainCarsOutput(!((IFTTTContainer.That.Minecraft.RedStoneOutput) this.ifcb).isTrainCarsOutput());
							return;
					}
					break;
				case 221:
					switch (button.id) {
						case 1000:
							DataType dataType = ((IFTTTContainer.That.RTM.DataMap) this.ifcb).getDataType();
							((IFTTTContainer.That.RTM.DataMap) this.ifcb).setDataType((DataType) KaizUtils.getNextEnum(dataType));
							break;
					}
					break;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}