package de.keygalp.mineX.entities;

import java.text.NumberFormat;
import java.util.Locale;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

import de.keygalp.mineX.Block;
import de.keygalp.mineX.Game;
import de.keygalp.mineX.fontMeshCreator.GUIText;
import de.keygalp.mineX.fontMeshCreator.TextMeshCreator;
import de.keygalp.mineX.guis.Chat;
import de.keygalp.mineX.guis.HUDManager;
import de.keygalp.mineX.input.KeyboardHandler;
import de.keygalp.mineX.input.MouseHandler;
import de.keygalp.mineX.inventory.InventoryHolder;
import de.keygalp.mineX.inventory.ItemStack;
import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.inventory.PlayerInventory;
import de.keygalp.mineX.models.TexturedModel;
import de.keygalp.mineX.utils.Ray;
import de.keygalp.mineX.worlds.Direction;
import de.keygalp.mineX.worlds.World;

public class Player extends Entity implements InventoryHolder {

	private Game game;
	private GUIText pitchYawText, dirText, focusedBlockText, velocityText;

	private Vector3i focusedBlock = new Vector3i();
	private Vector3i indirectlyFocusedBlock = new Vector3i();

	private Vector3f cameraOffset;

	private boolean inventoryOpen = false;

	private static float RANGE = 10;

	private static final float TERMINAL_VELOCITY = 3;
	private static final float WALKING_SPEED = 0.01f;
	private static final float JUMPING_FORCE = 1.0f;

	private PlayerInventory inventory;

	public Player(Game game, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale, new BoundingBox(new Vector3f(position),
				new Vector3f(-0.25f, 0, -0.25f), new Vector3f(0.5f, 2f, 0.5f)));
		this.game = game;
		pitchYawText = new GUIText("Pitch / YAW: ", 0.025f, new Vector2i(1000, 0));
		dirText = new GUIText("Pitch / YAW: ", 0.025f,
				new Vector2i(1000, TextMeshCreator.getPixelLineHeight(0.025f) * 1));
		focusedBlockText = new GUIText("Looking at: ", 0.025f,
				new Vector2i(1000, TextMeshCreator.getPixelLineHeight(0.025f) * 2));
		velocityText = new GUIText(
				"Vel: " + velocity.x + "," + velocity.y + "," + velocity.z + " (" + velocity.length() + ")", 0.025f,
				new Vector2i(1000, TextMeshCreator.getPixelLineHeight(0.025f) * 3));

		inventory = new PlayerInventory(36, "Player", this);

		cameraOffset = new Vector3f(0, 1.75f, 0);
	}

	@Override
	public void tick() {

		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_E)) {
			if (!Chat.visible) {
				inventoryOpen = !inventoryOpen;
				game.setPlayerFocused(!inventoryOpen);
				HUDManager.setCrosshairVisible(!inventoryOpen);
			}
		}

		accelleration.set(0, 0, 0);

		if (game.isPlayerFocused()) {
			control();
			act();
		}
		
		if (!onGround) {
			accelleration.y += World.GRAVITY;
		}

		velocity.add(accelleration);

		velocity.mul(World.AIR_RESITANCE);
		if(onGround) {
			velocity.mul(0.9f);
		}
		
		float termFac = velocity.length() / TERMINAL_VELOCITY;
		if (termFac > 1) {
			velocity.mul(1 / termFac);
		}

		
		
		position.add(velocity);

		blockPosition.set((int) Math.floor(position.x), (int) Math.floor(position.y), (int) Math.floor(position.z));

		bounds.setPos(new Vector3f(position));
		checkCollision();
		

		pitchYawText.update("Pitch / Yaw: " + Math.round(pitch) + " / " + Math.round(yaw));
		dirText.update("Facing:" + Direction.getName(Direction.facingDirection(yaw)));
		if (focusedBlock != null)
			focusedBlockText.update("Looking at: " + focusedBlock.x + "," + focusedBlock.y + "," + focusedBlock.z
					+ " : " + Game.getWorld().getBlock(focusedBlock));
		else
			focusedBlockText.update("Looking at: Null");
		velocityText.update(onGround + " / Vel: " + velocity.toString(NumberFormat.getNumberInstance()) + String.format("(%3.3f)", velocity.length()));
	}

	public void checkCollision() {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 2; j++) {
				for (int k = -1; k <= 1; k++) {
					Material m = Game.getWorld().getBlock(blockPosition.x + i, blockPosition.y + j,
							blockPosition.z + k);
					if (m != Material.AIR) {
						BoundingBox blockBounds = new BoundingBox(
								new Vector3f(blockPosition.x + i, blockPosition.y + j, blockPosition.z + k),
								new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
						
						if (bounds.collide(blockBounds)) {
							if (bounds.collideY(blockBounds)) {
								System.out.println("CollisionY:" + blockBounds.getPos().toString(NumberFormat.getIntegerInstance(Locale.GERMAN)));
								position.y -= velocity.y;
								
								velocity.y = 0;
								if(j <= 0) {
									onGround = true;
								}
								//bounds.setPos(new Vector3f(position));
							} 
							if (bounds.collideX(blockBounds)) {
								System.out.println("CollisionX:" + blockBounds.getPos().toString(NumberFormat.getIntegerInstance()));
								position.x -= velocity.x;
								velocity.x = 0;
								//onGround = true;
								//bounds.setPos(new Vector3f(position));
							}

							if (bounds.collideZ(blockBounds)) {
								System.out.println("CollisionZ:" + blockBounds.getPos().toString(NumberFormat.getIntegerInstance()));
								position.z -= velocity.z;
								velocity.z = 0;
								//onGround = true;
								//bounds.setPos(new Vector3f(position));
							}
						}
					} 
				}
			}
		}
	}

	public void clampCamera(Camera camera) {
		camera.setPosition(new Vector3f(position).add(cameraOffset));
		camera.setPitch(pitch);
		camera.setYaw(yaw);
	}

	public void act() {
		setFocusedBlock();
		if (MouseHandler.buttonTyped(GLFW.GLFW_MOUSE_BUTTON_1)) {
			if (focusedBlock != null) {
				Block block = new Block(focusedBlock, Game.getWorld().getBlock(focusedBlock));

				inventory.addItem(new ItemStack(block.getMaterial(), 1));

				Game.getWorld().setBlock(focusedBlock, Material.AIR);
			}
		}
		if (MouseHandler.buttonTyped(GLFW.GLFW_MOUSE_BUTTON_2)) {
			if (indirectlyFocusedBlock != null)
				Game.getWorld().setBlock(indirectlyFocusedBlock, Material.STONE);
		}
	}

	public void setFocusedBlock() {
		Vector3f dir = getNormalizedLookingDirection();
		for (int i = 0; i * Ray.INCREMENT <= RANGE; i++) {
			Vector3f rayPos = Ray.cast3D(new Vector3f(position).add(cameraOffset), dir, i);

			Vector3i blockPos = new Vector3i((int) Math.floor(rayPos.x + 0.00001), (int) Math.floor(rayPos.y + 0.00001),
					(int) Math.floor(rayPos.z + 0.00001));

			Material b = Game.getWorld().getBlock(blockPos);
			if (b != Material.AIR) {
				focusedBlock = blockPos;
				indirectlyFocusedBlock = Ray.blockCast(new Vector3f(position).add(cameraOffset), dir, i - 1);
				// System.out.println(rayPos.toString(NumberFormat.getNumberInstance()));
				return;
			}
		}
		focusedBlock = null;
		indirectlyFocusedBlock = null;
	}

	public Vector3f getNormalizedLookingDirection() {
		double radYaw = Math.toRadians(yaw - 90);
		double radPitch = -Math.toRadians(pitch);
		float x = (float) (Math.cos(radYaw) * Math.cos(radPitch));
		float z = (float) (Math.sin(radYaw) * Math.cos(radPitch));
		float y = (float) Math.sin(radPitch);
		Vector3f vec = new Vector3f(x, y, z);
		vec = vec.normalize();
		return vec;
	}

	public void control() {

		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
			float dx = (float) (WALKING_FORCE * Math.sin(Math.toRadians(yaw)));
			float dz = (float) (-WALKING_FORCE * Math.cos(Math.toRadians(yaw)));
			accelleration.x += dx;
			accelleration.z += dz;
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
			float dx = (float) (WALKING_FORCE * Math.sin(Math.toRadians(yaw - 180)));
			float dz = (float) (-WALKING_FORCE * Math.cos(Math.toRadians(yaw - 180)));
			accelleration.x += dx;
			accelleration.z += dz;
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
			float dx = (float) (WALKING_FORCE * Math.sin(Math.toRadians(yaw - 90)));
			float dz = (float) (-WALKING_FORCE * Math.cos(Math.toRadians(yaw - 90)));
			accelleration.x += dx;
			accelleration.z += dz;
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
			float dx = (float) (WALKING_FORCE * Math.sin(Math.toRadians(yaw + 90)));
			float dz = (float) (-WALKING_FORCE * Math.cos(Math.toRadians(yaw + 90)));
			accelleration.x += dx;
			accelleration.z += dz;
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			if (onGround) {
				accelleration.y = JUMPING_FORCE;
				onGround = false;
			}
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			accelleration.y -= WALKING_FORCE;
		}

		Vector2f v = MouseHandler.getMovement();
		float dPitch = (float) (v.y * SENSITIVITY);
		float dYaw = (float) (v.x * SENSITIVITY);
		pitch += dPitch;
		yaw += dYaw;

		if (pitch > 90)
			pitch = 90;
		if (pitch < -90)
			pitch = -90;

		if (yaw > 180)
			yaw -= 360;
		if (yaw < -180)
			yaw += 360;

	}

	public void teleport(Vector3f pos) {
		this.position = pos;
	}

	@Override
	public PlayerInventory getInventory() {
		return inventory;
	}
	
	public Vector3i getBlockPosition() {
		return blockPosition;
	}

}
