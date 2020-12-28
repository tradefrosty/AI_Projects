/*
 * Jacob Woodhouse
 * 
 * 12-4-20
 */

using UnityEngine;
using System;
using System.Linq;

public class AIscript_JacobWoodhouse : MonoBehaviour
{
    public CharacterScript mainScript;
    public float[] bombSpeeds;
    public float[] buttonCooldowns;
    public float[] buttonDistance = new float[8];
    public float[] bombDistances;
    public float[] buttonLocations;
    public float opponentLocation;
    public int[] beltDirections;
    public float lastLocation = 0;
    public uint directUp = 0b_0;
    public float playerSpeed;
    public float playerLocation;
    public float target;
    public int count;
    public int destroy;
    public int locationDifference;
    public float[] attackImportance = new float[8];
    public bool blue;
    public bool red;
    public bool crossOver;
    public bool attack;
   

    //This method is used to initilize the game
    void Start()
        {
        mainScript = GetComponent<CharacterScript>();

        if (mainScript == null)//If there is not a script to run
            {
            print("No CharacterScript found on " + gameObject.name);//Error messages are printed
            this.enabled = false;
            }

        buttonLocations = mainScript.getButtonLocations();//Uses the main script to get the locations of the button

        playerSpeed = mainScript.getPlayerSpeed();//calls the getPlayerSpeed method to get the speed of the player to increase or decrease
        }

    // Update is called once per frame
    void Update()
        {
        buttonCooldowns = mainScript.getButtonCooldowns();
        beltDirections = mainScript.getBeltDirections();
        playerLocation = mainScript.getCharacterLocation();
        bombDistances = mainScript.getBombDistances();
        bombSpeeds = mainScript.getBombSpeeds();
        opponentLocation = mainScript.getOpponentLocation();
        destroy = AttackTarget();
        SpamBombs();

    if (attack == true) {
         if (buttonLocations[destroy] + .1 >= playerLocation)
        {
            if (buttonDistance[destroy] <= .9) 
                mainScript.push();
                mainScript.moveUp();
        }
        else if (buttonLocations[destroy] - .1 <= playerLocation)
        {
            if (buttonDistance[destroy] <= .9)
                mainScript.push();
                mainScript.moveDown();

        }
        StepsToButton();  
        SetTarget();

    }
        }

    void StepsToButton()
    {
        for (int i = 0; i < 8; i++)
            {
            buttonDistance[i] = Math.Abs(buttonLocations[i] - playerLocation);  
            }
    }
    //This controls when the AI switches targets
    int AttackTarget()
    {
        if ((beltDirections[destroy]) > 0 && (bombSpeeds[destroy] >= 4)) {
            destroy = Array.IndexOf(attackImportance, attackImportance.Min());
        } else {
            destroy = Array.IndexOf(attackImportance, attackImportance.Min());
        }
        return destroy;
    }
    //The purpose of this method is to score the importance on which bomb to attack.
    void SetTarget()
    {
        target = 0;
        for (int i = 0; i < buttonLocations.Length; i++)
            {   
            if (buttonCooldowns[i] >= .8)
                {
                target += 2;
                }   
            target = target + (bombSpeeds[i] * beltDirections[i]);
            attackImportance[i] = target;
            target = 0;
        }
    }
    //This method makes the AI spam bombs until it comes into contact with the opponent 
    // and also determines which side the AI is on. 
    void SpamBombs(){
        count += 1;

        if(attack == false) {
            mainScript.push();
            if (red)
                mainScript.moveDown();
        
            if (blue)
                mainScript.moveUp();
    }

    if ((playerLocation < opponentLocation) && (count == 1))
        {
         blue = true;
        } 

        if ((playerLocation > opponentLocation) && (count == 1))
        {
        red = true;
        } 

    if((blue == true) && (attack == false)){
        if (playerLocation > opponentLocation){
             attack = true;
        }
    }
    if((red == true) && (attack == false)){
        if (playerLocation < opponentLocation){
             attack = true;
        }
    }

    }
}
