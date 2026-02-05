package cloud.grabsky.displayentities.spec;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import revxrsal.spec.ArrayCommentStyle;
import revxrsal.spec.CommentedConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public final class ConfigurationHelper {

    private static final Yaml YAML;

    static {
        final DumperOptions options = new DumperOptions();
        options.setSplitLines(false);
        options.setProcessComments(false);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(new QuotedStringRepresenter(options), options);
    }

    /**
     * Returns {@link CommentedConfiguration} instance at provided {@link File}.
     */
    public static @NotNull CommentedConfiguration fromFile(final @NotNull File file) {
        return fromPath(file.toPath());
    }

    /**
     * Returns {@link CommentedConfiguration} instance at provided {@link Path}.
     */
    public static @NotNull CommentedConfiguration fromPath(final @NotNull Path path) {
        return new CommentedConfiguration(path, CommentedConfiguration.GSON, ArrayCommentStyle.COMMENT_FIRST_ELEMENT, YAML);
    }


    /**
     * This {@link Representer} instance is responsible for double-quoting all String values.
     */
    private static final class QuotedStringRepresenter extends Representer {

        private QuotedStringRepresenter(final DumperOptions options) {
            super(options);
        }

        @Override
        protected MappingNode representMapping(final Tag tag, final @NotNull Map<?, ?> mapping, final DumperOptions.FlowStyle flowStyle) {
            final List<NodeTuple> value = new ArrayList<>(mapping.size());
            mapping.forEach((mappingKey, mappingValue) -> {
                final Node nodeValue = (mappingValue instanceof String)
                        // Double-quoting if instance of String.
                        ? new ScalarNode(Tag.STR, (String) mappingValue, null, null, DumperOptions.ScalarStyle.DOUBLE_QUOTED)
                        // Otherwise, delegating to fallback.
                        : representData(mappingValue);
                // Adding to the list.
                value.add(new NodeTuple(representData(mappingKey), nodeValue));
            });
            return new MappingNode(tag, value, flowStyle);
        }

        @Override
        protected Node representSequence(final Tag tag, final @NotNull Iterable<?> sequence, final DumperOptions.FlowStyle flowStyle) {
            final List<Node> value = new ArrayList<>();
            for (final Object item : sequence) {
                final Node nodeItem = (item instanceof String)
                        // Double-quoting if instance of String.
                        ? new ScalarNode(Tag.STR, (String) item, null, null, DumperOptions.ScalarStyle.DOUBLE_QUOTED)
                        // Otherwise, delegating to fallback.
                        : representData(item);
                // Adding to the list.
                value.add(nodeItem);
            }
            return new SequenceNode(tag, value, flowStyle);
        }

    }
}
