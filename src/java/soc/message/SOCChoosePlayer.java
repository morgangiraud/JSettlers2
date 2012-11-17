/**
 * Java Settlers - An online multiplayer version of the game Settlers of Catan
 * Copyright (C) 2003  Robert S. Thomas <thomas@infolab.northwestern.edu>
 * Portions of this file Copyright (C) 2010-2012 Jeremy D Monin <jeremy@nand.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The maintainer of this program can be reached at jsettlers@nand.net
 **/
package soc.message;

import java.util.StringTokenizer;


/**
 * This message from client to server has a few purposes, all related to robbing:
 *<UL>
 *<LI> After a server's {@link SOCChoosePlayerRequest}, 
 *     it says which player the current player wants to
 *     steal from.
 *<LI> After a server's {@link SOCGameState}
 *     ({@link soc.game.SOCGame#WAITING_FOR_ROBBER_OR_PIRATE WAITING_FOR_ROBBER_OR_PIRATE}) message,
 *     it says whether the player wants to move the robber
 *     or the pirate ship. (v2.0.00+)
 *<LI> After a server's {@link SOCChoosePlayer} message, it says whether the player wants to
 *     rob cloth or rob a resource from the victim. (v2.0.00+)
 *</UL>
 * {@link #getChoice()} gets the client's choice.
 *<P>
 * Also sent from server to client (v2.0.00+) to prompt the client to choose to rob
 * cloth or rob a resource from the victim; {@link #getChoice()} is the victim player number.
 *
 * @author Robert S. Thomas &lt;thomas@infolab.northwestern.edu&gt;
 */
public class SOCChoosePlayer extends SOCMessage
    implements SOCMessageForGame
{
    /**
     * Name of game
     */
    private String game;

    /**
     * The number of the chosen player,
     * or -1 to move the robber
     * or -2 to move the pirate ship.
     */
    private int choice;

    /**
     * Create a ChoosePlayer message.
     *
     * @param ga  the name of the game
     * @param ch  the number of the chosen player,
     *   or -1 to move the robber
     *   or -2 to move the pirate ship.
     *<br>
     * For <tt>WAITING_FOR_ROB_CLOTH_OR_RESOURCE</tt>, use <tt>ch</tt> = playerNumber
     * to rob a resource; to rob cloth, use <tt>ch</tt> = -(playerNumber + 1).
     */
    public SOCChoosePlayer(String ga, int ch)
    {
        messageType = CHOOSEPLAYER;
        game = ga;
        choice = ch;
    }

    /**
     * @return the name of the game
     */
    public String getGame()
    {
        return game;
    }

    /**
     * @return the number of the chosen player,
     *   or -1 to move the robber
     *   or -2 to move the pirate ship.
     *<br>
     * For <tt>WAITING_FOR_ROB_CLOTH_OR_RESOURCE</tt>, <tt>getChoice()</tt> &gt;= 0
     * means rob a resource from that player number, and <tt>getChoice()</tt> &lt; 0
     * means rob cloth from player number (<tt>-getChoice()</tt>) - 1.
     * 
     */
    public int getChoice()
    {
        return choice;
    }

    /**
     * CHOOSEPLAYER sep game sep2 choice
     *
     * @return the command string
     */
    public String toCmd()
    {
        return toCmd(game, choice);
    }

    /**
     * CHOOSEPLAYER sep game sep2 choice
     *
     * @param ga  the name of the game
     * @param ch  the number of the chosen player;
     *            see {@link #SOCChoosePlayer(String, int)} for meaning
     * @return the command string
     */
    public static String toCmd(String ga, int ch)
    {
        return CHOOSEPLAYER + sep + ga + sep2 + ch;
    }

    /**
     * Parse the command String into a ChoosePlayer message
     *
     * @param s   the String to parse
     * @return    a ChoosePlayer message, or null of the data is garbled
     */
    public static SOCChoosePlayer parseDataStr(String s)
    {
        String ga; // the game name
        int ch; // the number of the chosen player 

        StringTokenizer st = new StringTokenizer(s, sep2);

        try
        {
            ga = st.nextToken();
            ch = Integer.parseInt(st.nextToken());
        }
        catch (Exception e)
        {
            return null;
        }

        return new SOCChoosePlayer(ga, ch);
    }

    /**
     * @return a human readable form of the message
     */
    public String toString()
    {
        return "SOCChoosePlayer:game=" + game + "|choice=" + choice;
    }
}
