package {package};

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;
import de.edgelord.saltyengine.displaymanager.display.SplashWindow;
import de.edgelord.saltyengine.scene.SceneManager;

public class Main extends Game {

    public static void main(String[] args) {
        init(GameConfig.config({resWidth}, {resHeight}, "{gameName}", {fixedMillis}));
        {start}
    }
}