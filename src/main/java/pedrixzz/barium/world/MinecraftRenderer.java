package pedrixzz.barium.world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class MinecraftRenderer {

    private List<Chunk> chunks = new ArrayList<>();

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        for (Chunk chunk : chunks) {
            if (chunk.isVisible()) {
                chunk.render();
            }
        }
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public void removeChunk(Chunk chunk) {
        chunks.remove(chunk);
    }

    private class Chunk {

        private int x;
        private int z;
        private boolean visible;

        public Chunk(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public boolean isVisible() {
            // Implementar lógica para determinar se o chunk é visível
            return true;
        }

        public void render() {
            // Implementar lógica para renderizar o chunk
        }
    }
}
