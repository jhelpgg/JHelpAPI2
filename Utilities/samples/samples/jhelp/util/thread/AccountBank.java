package samples.jhelp.util.thread;

import jhelp.util.thread.Mutex;

public class AccountBank
{
    private       int   money;
    private final Mutex mutex;

    public AccountBank(int money)
    {
        this.mutex = new Mutex();
        this.money = money;
    }

    public void put(int amount)
    {
        this.mutex.playInCriticalSectionVoid(
                amount1 ->
                {
                    if (amount1 <= 0)
                    {
                        return;
                    }

                    this.money += amount1;
                }, amount);
    }

    public int take(int desiredAmount)
    {
        if (desiredAmount <= 0)
        {
            return 0;
        }

        return this.mutex.playInCriticalSection(
                amount ->
                {
                    if (this.money < amount)
                    {
                        int give = this.money;
                        this.money = 0;
                        return give;
                    }

                    this.money -= amount;
                    return amount;
                },
                desiredAmount);
    }
}
