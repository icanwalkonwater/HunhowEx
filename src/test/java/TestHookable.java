import com.jesus_crie.hunhowex.hooks.Hookable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class TestHookable extends Hookable {

    private String content;

    public TestHookable(long id, String content) {
        this.content = content;
    }

    @Override
    public MessageEmbed serialize() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(content);

        return builder.build();
    }
}
