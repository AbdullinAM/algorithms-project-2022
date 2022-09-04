import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Node<T>
{

    static <R> void wire(@NotNull Node<R> from, @Nullable Node<R> to, int atLevel)
    {
        if (to == null)
        {
            from.childAtLevel.remove(atLevel);
        }
        else
        {
            // from -> to
            if (from.childAtLevel.size() > atLevel)
                from.childAtLevel.set(atLevel, to);
            else
                from.childAtLevel.add(atLevel, to);
            // from <- to
            if (to.parentAtLevel.size() > atLevel)
                to.parentAtLevel.set(atLevel, from);
            else
                to.parentAtLevel.add(atLevel, from);
        }
    }

    private final @NotNull ArrayList<Node<T>> childAtLevel;
    private final @NotNull ArrayList<Node<T>> parentAtLevel;
    private final @NotNull T key;

    Node(@NotNull T key) {
        this.key = key;
        this.childAtLevel = new ArrayList<>();
        this.parentAtLevel = new ArrayList<>();
    }

    @NotNull T key() {
        return this.key;
    }

    @NotNull Node<T> next(int atLevel) {
        return childAtLevel.get(atLevel);
    }

    @Nullable Node<T> child(int atLevel)
    {
        return (hasNext(atLevel)) ? next(atLevel) : null;
    }

    boolean hasNext(int atLevel)
    {
        return atLevel < childAtLevel.size();
    }

    boolean hasPrev(int atLevel)
    {
        return parentAtLevel.size() > atLevel;
    }

    @NotNull Node<T> prev(int atLevel)
    {
        return parentAtLevel.get(atLevel);
    }

    @Nullable Node<T> parent(int atLevel)
    {
        return (hasPrev(atLevel)) ? prev(atLevel) : null;
    }

    @NotNull Node<T> nextLowest() {
        return next(0);
    }

    void connect(@NotNull Node<T> child, int atLevel)
    {
        if (atLevel > childAtLevel.size())
            throw new RuntimeException("Semantic error: " + this + " to " + child + " at level " + atLevel);
        final @Nullable Node<T> hisParent = child.parent(atLevel);
        if (hisParent != null)
            wire(hisParent, this, atLevel);
        wire(this, child, atLevel);
    }

    int childLevelsSize()
    {
        return childAtLevel.size();
    }

    int parentLevelSize()
    {
        return parentAtLevel.size();
    }

    @Override
    public String toString()
    {
        return key.toString();
    }
}
