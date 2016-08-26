package com.mw.logic.characters.info;

/**
 * Created by BanditCat on 2016/7/25.
 */
abstract public class CharacterInfo {
    protected String name = "";//名字
    protected int healthPoint = 0;//血量
    protected int attackPoint = 0;//攻击力
    protected int attackRange = 0;//攻击范围
    protected int defensePoint = 0;//防御力
    protected int speed = 0;//速度
    protected String describe = "";//描述

    public CharacterInfo(){

    }
    public CharacterInfo(String name,int h,int ap,int ar,int dp,int sp){
        this.name = name;
        this.healthPoint = h;
        this.attackPoint = ap;
        this.attackRange = ar;
        this.defensePoint = dp;
        this.speed = sp;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDefensePoint() {
        return defensePoint;
    }

    public void setDefensePoint(int defensePoint) {
        this.defensePoint = defensePoint;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
