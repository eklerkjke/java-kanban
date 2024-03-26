package manager;

import provider.Managers;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    protected TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }
}
