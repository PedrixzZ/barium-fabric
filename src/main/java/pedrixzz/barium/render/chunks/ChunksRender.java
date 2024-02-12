package com.example.optimizechunks;

import com.fabricmc.api.ClientMod;
import com.fabricmc.api.ModInitializer;
import com.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

@ClientMod
public class ChunksRender implements ModInitializer {

    private static final int CHUNK_LOAD_DISTANCE = 8; // Ajustar conforme necessário
    private static final int CHUNK_REQUEST_DELAY = 200; // Ajustar conforme necessário
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("OptimizeChunks-Thread");
            return thread;
        }
    });

    private static final Map<ChunkPos, CompletableFuture<Chunk>> chunkFutures = new HashMap<>();

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;

            // Obter a posição do chunk do jogador
            ChunkPos playerChunkPos = new ChunkPos(player.getX(), player.getZ());

            // Carregar chunks próximos
            for (int x = playerChunkPos.x - CHUNK_LOAD_DISTANCE; x <= playerChunkPos.x + CHUNK_LOAD_DISTANCE; x++) {
                for (int z = playerChunkPos.z - CHUNK_LOAD_DISTANCE; z <= playerChunkPos.z + CHUNK_LOAD_DISTANCE; z++) {
                    world.getChunk(x, z);
                }
            }

            // Solicitar chunks específicos em segundo plano
            for (int x = playerChunkPos.x - CHUNK_LOAD_DISTANCE * 2; x <= playerChunkPos.x + CHUNK_LOAD_DISTANCE * 2; x++) {
                for (int z = playerChunkPos.z - CHUNK_LOAD_DISTANCE * 2; z <= playerChunkPos.z + CHUNK_LOAD_DISTANCE * 2; z++) {
                    ChunkPos chunkPos = new ChunkPos(x, z);
                    if (!chunkFutures.containsKey(chunkPos)) {
                        // Agendar solicitação de chunk com atraso
                        executorService.schedule(() -> requestChunk(chunkPos), CHUNK_REQUEST_DELAY, TimeUnit.MILLISECONDS);
                    }
                }
            }
        });

        // Registrar o manipulador de pacote do servidor
        ClientPlayNetworking.registerGlobalReceiver(C2SPacketHandler.ID, (client, packet, sender) -> {
            // Decodificar o buffer de pacote e recuperar os dados do chunk
            PacketByteBuf buf = new PacketByteBuf(packet);
            int chunkX = buf.readInt();
            int chunkZ = buf.readInt();
            byte[] chunkData = buf.readByteArray();

            // Completar o futuro do chunk com os dados recebidos
            ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
            CompletableFuture<Chunk> chunkFuture = chunkFutures.get(chunkPos);
            if (chunkFuture != null) {
                chunkFuture.complete(new Chunk(world, chunkData));
            }
        });
    }

    private static void requestChunk(ChunkPos chunkPos) {
        // Criar um futuro para o chunk
        CompletableFuture<Chunk> chunkFuture = new CompletableFuture<>();
        chunkFutures.put(chunkPos, chunkFuture);
        // Enviar pacote ao servidor solicitando o chunk
        PacketByteBuf buf = new PacketByteBuf(ClientPlayNetworking.createC2SPacket(C2SPacketHandler.ID));
        buf.writeInt(chunkPos.x);
        buf.writeInt(chunkPos.z);
        ClientPlayNetworking.send(buf);

        // Processar o chunk quando disponível
        chunkFuture.whenComplete((chunk, throwable) -> {
            if (chunk != null) {
                // Utilizar o chunk carregado (ex: renderizar, etc.)
                // ...
            } else {
                // Erro ao carregar o chunk: lidar com o caso
                System.err.println("Erro ao carregar chunk " + chunkPos);
            }
            chunkFutures.remove(chunkPos);
        });
    }
}
