package me.ninjamandalorian.implodusports.ui;

import me.ninjamandalorian.implodusports.ui.tasks.BaseTask;
import me.ninjamandalorian.implodusports.ui.tasks.PageTask;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class BaseMenu implements InventoryHolder {

	// Menu fields
	private Inventory inventory;
	private HashMap<Integer, BaseButton> menuButtons;
	private String openMessage = null;

	// Paged menu fields
	private ArrayList<ArrayList<BaseButton>> pages = null; // If null, menu is not paged
	private int currentPage = 0;

	// MENU FUNCTIONS //

	/**
	 * Builds menu using non-paged builder
	 *
	 * @param builder - Builder used
	 */
	private BaseMenu(Builder builder) {
		this.inventory = Bukkit.createInventory(this, builder.menuSize, builder.menuTitle);

		// Creates all items in inventory
		for (Entry<Integer, BaseButton> buttonEntry : builder.menuButtons.entrySet()) {
			if (buttonEntry.getKey() >= builder.menuSize) continue;
			inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItemStack());
		}

		this.menuButtons = builder.menuButtons;
		this.openMessage = builder.openMsg;
	}

	/**
	 * Builds menu using paged builder
	 *
	 * @param pagedBuilder - Builder used
	 */
	private BaseMenu(PagedBuilder pagedBuilder) {
		this.inventory = Bukkit.createInventory(this, pagedBuilder.menuSize, pagedBuilder.menuTitle);

		// Creates all items in inventory
		for (Entry<Integer, BaseButton> buttonEntry : pagedBuilder.menuButtons.entrySet()) {
			if (buttonEntry.getKey() >= pagedBuilder.menuSize) continue;
			inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItemStack());
		}

		this.menuButtons = pagedBuilder.menuButtons;
		this.openMessage = pagedBuilder.openMsg;

		this.pages = pagedBuilder.pageButtons;
		this.loadPage(0);
	}

	/**
	 * Created a non-paged builder
	 *
	 * @return Builder
	 */
	public static Builder createBuilder() {
		return new Builder();
	}


	// PAGED MENU FUNCTIONS //

	/**
	 * Creates a paged builder
	 *
	 * @return Builder
	 */
	public static PagedBuilder createPagedBuilder() {
		return new PagedBuilder();
	}

	/**
	 * Opens the menu for a specific player.
	 *
	 * @param player - Player to have menu
	 */
	public void open(Player player) {
		player.openInventory(inventory);
		if (this.openMessage != null)
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', openMessage));
	}

	// BUILDERS FOR MENUS //

	/**
	 * Gets the inventory of the menu
	 */
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	/**
	 * Gets button, indexed from 0:(size-1)
	 *
	 * @param slotNum - Slot number
	 * @return Button of slot or null if empty.
	 */
	public BaseButton getButton(int slotNum) {
		if (slotNum > this.inventory.getSize() - 1) return null;
		return this.menuButtons.get(slotNum);
	}

	public void changePage(Player player, int direction) {
		if (pages == null) return;
		currentPage += direction;
		currentPage = Math.max(currentPage, 0);
		currentPage = Math.min(currentPage, pages.size() - 1);

		loadPage(currentPage);
	}

	private void loadPage(int pageNum) {
		//Logger.debug("LOADING PAGE " + pageNum);
		List<Integer> slotList = Arrays.asList(
			10, 11, 12, 13, 14, 15, 16,
			19, 20, 21, 22, 23, 24, 25,
			28, 29, 30, 31, 32, 33, 34,
			37, 38, 39, 40, 41, 42, 43);

		ArrayList<BaseButton> pageButtons = pages.get(pageNum);
		for (int i = 0; i < 28; i++) {
			if (i < pageButtons.size()) {
				inventory.setItem(slotList.get(i), pageButtons.get(i).getItemStack());
				menuButtons.put(slotList.get(i), pageButtons.get(i));
			} else {
				inventory.setItem(slotList.get(i), null);
				menuButtons.remove(slotList.get(i));
			}
		}
	}

	/**
	 * Menu Builder
	 */
	public static class Builder {
		private String menuTitle = "ip-title";
		private int menuSize = 54;
		private HashMap<Integer, BaseButton> menuButtons = new HashMap<Integer, BaseButton>();
		private String openMsg = null;

		Builder() {
		}

		public Builder title(String title) {
			this.menuTitle = ChatColor.translateAlternateColorCodes('&', title);
			return this;
		}

		public Builder rows(int num) {
			this.menuSize = 9 * Math.min(num, 6);
			return this;
		}

		public Builder setButton(int slot, BaseButton button) {
			this.menuButtons.put(slot, button);
			return this;
		}

		// So you don't have to do a useless instance creation in the building phase.
		public Builder setButton(int slot, ItemStack itemStack, BaseTask task) {
			this.menuButtons.put(slot, BaseButton.create().itemStack(itemStack).task(task));
			return this;
		}

		public BaseMenu build() {
			return new BaseMenu(this);
		}

		public Builder fillRow(int row) {

			for (int i = 0; i < 9; i++) {
				int slot = 9 * row + i;
				if (!this.menuButtons.containsKey(slot)) {
					this.menuButtons.put(slot, BaseButton.background());
				}
			}

			return this;
		}

		public Builder fillColumn(int column) {

			for (int i = 0; i < (this.menuSize / 9); i++) {
				int slot = 9 * i + column;
				if (!this.menuButtons.containsKey(slot)) {
					this.menuButtons.put(slot, BaseButton.background());
				}
			}

			return this;
		}

		public Builder fillOutline() {
			return this.fillColumn(0).fillColumn(8).fillRow(0).fillRow((this.menuSize / 9) - 1);
		}

		public Builder openMsg(String msg) {
			this.openMsg = msg;
			return this;
		}
	}

	/**
	 * Paged Menu Builder
	 * <br> NOTE: These are always 6 rows tall for calculation reasons.
	 *
	 * @author NinjaMandalorian
	 */
	public static class PagedBuilder {
		private String menuTitle = "ip-title";
		private int menuSize = 54;
		private HashMap<Integer, BaseButton> menuButtons = new HashMap<Integer, BaseButton>();
		private ArrayList<ArrayList<BaseButton>> pageButtons = new ArrayList<ArrayList<BaseButton>>();
		private String openMsg = null;

		PagedBuilder() {
			List<Integer> outlineSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44,
				45, 46, 47, 48, 49, 50, 51, 52, 53);
			for (Integer slot : outlineSlots) {
				menuButtons.put(slot, BaseButton.background());
			}
		}

		public PagedBuilder title(String title) {
			this.menuTitle = ChatColor.translateAlternateColorCodes('&', title);
			return this;
		}

		public PagedBuilder setButton(int slot, BaseButton button) {
			menuButtons.put(slot, button);
			return this;
		}

		/**
		 * Splits buttons into pages for the menu.
		 *
		 * @param buttonList - List of buttons entered
		 * @return Builder
		 */
		public PagedBuilder setContents(ArrayList<BaseButton> buttonList) {
			int pageNum = Math.floorDiv(buttonList.size(), 28) + 1;

			for (int i = 0; i < pageNum; i++) {
				ArrayList<BaseButton> page = new ArrayList<BaseButton>();

				for (int j = 0; j < 28; j++) {
					if (28 * i + j >= buttonList.size()) break;
					page.add(buttonList.get(28 * i + j));
				}
				pageButtons.add(page);
			}

			return this;
		}

		public PagedBuilder makePageButtons() {
			menuButtons.put(48, BaseButton.create(Material.GREEN_DYE).name("&ePrevious Page").task(new PageTask(-1)));
			menuButtons.put(50, BaseButton.create(Material.GREEN_DYE).name("&eNext Page").task(new PageTask(1)));
			return this;
		}

		public PagedBuilder openMsg(String msg) {
			this.openMsg = msg;
			return this;
		}

		public BaseMenu build() {
			return new BaseMenu(this);
		}
	}
}
