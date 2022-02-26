package dhyces.dinnerplate.blockentity.api;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SyncedBlockEntity extends BlockEntity {

	public SyncedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
		super(pType, pWorldPosition, pBlockState);
	}

	@Override
	public CompoundTag getUpdateTag() {
		var tag = saveWithFullMetadata();
		writeClient(tag);
		return tag;
	}
	
//	@Override
//	public void handleUpdateTag(CompoundTag tag) {
//		super.handleUpdateTag(tag);
//		readClient(tag);
//	}
//
//	@Override
//	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//		super.onDataPacket(net, pkt);
//	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/** Save additional information, such as itemstacks or fluidstacks*/
	public abstract void write(CompoundTag tag);

	/** Load additional information, such as itemstacks or fluidstacks*/
	public abstract void read(CompoundTag tag);
	
	/** Load additional information, such as itemstacks or fluidstacks, separate from the regular methods in case implementors wanted to send
	 *  more or less information*/
	public void writeClient(CompoundTag tag) {
		write(tag);
	}
	
	/** Load additional information, such as itemstacks or fluidstacks, separate from the regular methods in case implementors wanted to send
	 *  more or less information*/
	public void readClient(CompoundTag tag) {
		read(tag);
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		read(pTag);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		write(pTag);
	}

}
