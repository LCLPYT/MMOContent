package work.lclpnet.mmocontent.block;

import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

public class MMOSignType extends SignType {

    protected final Identifier id;

    public MMOSignType(Identifier id) {
        super(id.getPath());
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }
}
