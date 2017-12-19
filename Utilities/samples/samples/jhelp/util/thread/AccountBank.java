/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

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
