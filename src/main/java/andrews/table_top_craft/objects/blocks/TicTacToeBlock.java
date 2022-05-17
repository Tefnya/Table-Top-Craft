package andrews.table_top_craft.objects.blocks;

import andrews.table_top_craft.tile_entities.TicTacToeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TicTacToeBlock extends BaseEntityBlock
{
    private static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);

    public TicTacToeBlock()
    {
        super(getProperties());
    }

    private static Properties getProperties()
    {
        Properties properties = Block.Properties.of(Material.CLOTH_DECORATION);
        properties.strength(0.2F);
        properties.sound(SoundType.BIG_DRIPLEAF);
        return properties;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        return AABB;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        Direction face = hit.getDirection();
        if (face.equals(Direction.UP))
        {
            int tile = this.getTile(hit.getLocation());
            int column = this.getColumn(hit.getLocation());

            byte tileCoordinate = (byte) Mth.clamp((3 - tile) * 3 + column, 0, 9);


            BlockEntity blockEntity = level.getBlockEntity(pos);
            // We make sure the BlockEntity is a TicTacToeBlockEntity
            if(blockEntity instanceof TicTacToeBlockEntity ticTacToeBlockEntity)
            {
                char[] chars = ticTacToeBlockEntity.getTicTacToeGame().replaceAll("/", "").toCharArray();

                if(player.isShiftKeyDown())
                {
                    ticTacToeBlockEntity.setTicTacToeGame("---/---/---");
                    ticTacToeBlockEntity.resetFrames();
                    level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 2);
                    ticTacToeBlockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }

                if(player.getItemInHand(hand).getItem() instanceof DyeItem dyeItem)
                {
                    switch (dyeItem.getDyeColor())
                    {
                        case WHITE -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "255/255/255");
                        case ORANGE -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "255/145/0");
                        case MAGENTA -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "220/110/235");
                        case LIGHT_BLUE -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "100/212/250");
                        case YELLOW -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "255/255/0");
                        case LIME -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "0/255/0");
                        case PINK -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "255/148/220");
                        case GRAY -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "130/130/130");
                        case LIGHT_GRAY -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "200/200/200");
                        case CYAN -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "0/150/170");
                        case PURPLE -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "140/60/210");
                        case BLUE -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "52/52/255");
                        case BROWN -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "110/55/0");
                        case GREEN -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "75/130/0");
                        case RED -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "255/0/0");
                        case BLACK -> setProperColor(tileCoordinate - 1, ticTacToeBlockEntity, "0/0/0");
                    }
                    // Sync the Block
                    level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 2);

                    if(!player.isCreative())
                        player.getItemInHand(hand).shrink(1);
                    return InteractionResult.SUCCESS;
                }

                if(!ticTacToeBlockEntity.isGameOver(ticTacToeBlockEntity.getTicTacToeGame()))
                {
                    if (chars[tileCoordinate - 1] == '-')
                    {
                        chars[tileCoordinate - 1] = getEmptyTiles(chars) % 2 == 0 ? 'O' : 'X';
                        ticTacToeBlockEntity.setTicTacToeFrameAt(0, tileCoordinate - 1);
                    }

                    chars = new char[]{chars[0], chars[1], chars[2], '/', chars[3], chars[4], chars[5], '/', chars[6], chars[7], chars[8]};

                    ticTacToeBlockEntity.setTicTacToeGame(String.valueOf(chars));
                    level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 2);
                    ticTacToeBlockEntity.setChanged();
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void setProperColor(int tileCoordinate, TicTacToeBlockEntity ticTacToeBlockEntity, String color)
    {
        if(ticTacToeBlockEntity.getTicTacToeCharAt(tileCoordinate) != '-')
        {
            if (ticTacToeBlockEntity.getTicTacToeCharAt(tileCoordinate) == 'O')
            {
                ticTacToeBlockEntity.setCircleColor(color);
            }
            else
            {
                ticTacToeBlockEntity.setCrossColor(color);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TicTacToeBlockEntity(pos, state);
    }

    // We have to do this as we use both a JSON and Java model on the same block,
    // otherwise the block would only render one (The Java Model) so we specify both here.
    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return (level1, pos, state1, blockEntity) -> TicTacToeBlockEntity.tick(level1, pos, state1, (TicTacToeBlockEntity) blockEntity);
    }

    private int getTile(Vec3 vec3d)
    {
        double value = -vec3d.x;
        // We floor the value and subtract it from the original, that way we get rid of the block position value
        value -= Math.floor(value);
        // We multiply the value with 100 to make it easier to use
        value *= 100;

        return (int) (3 - Math.floor(value / 33.3D));
    }

    private int getColumn(Vec3 vec3d)
    {
        double value = -vec3d.z;
        // We floor the value and subtract it from the original, that way we get rid of the block position value
        value -= Math.floor(value);
        // We multiply the value with 100 to make it easier to use
        value *= 100;

        return (int) (3 - Math.floor(value / 33.3D));
    }

    private int getEmptyTiles(char[] game)
    {
        int emptyTiles = 0;
        for(char chars : game)
        {
            if(chars == '-')
                emptyTiles++;
        }
        return emptyTiles;
    }
}