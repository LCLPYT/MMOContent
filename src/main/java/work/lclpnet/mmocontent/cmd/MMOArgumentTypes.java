package work.lclpnet.mmocontent.cmd;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.asm.mixin.common.ArgumentTypesAccessor;

public class MMOArgumentTypes {

    public static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> void register(
            Identifier id, Class<? extends A> argClass, ArgumentSerializer<A, T> serializer
    ) {
        ArgumentTypesAccessor.invokeRegister(Registry.COMMAND_ARGUMENT_TYPE, id.toString(), argClass, serializer);
    }
}
