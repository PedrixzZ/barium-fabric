package pedrixzz.barium;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Barium implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        System.out.println("Barium carregado!");
    }
}
