package pedrixzz.barium;

import java.util.ArrayList;
import java.util.List;

public class WorldRender {

    private final List<Renderable> renderables = new ArrayList<>();

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public void render() {
        for (Renderable renderable : renderables) {
            renderable.render();
        }
    }

}

interface Renderable {

    void render();

}

class ExampleRenderable implements Renderable {

    @Override
    public void render() {
        worldRender.render();
    }

}
