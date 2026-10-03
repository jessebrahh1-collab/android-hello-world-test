package com.krustyk.cokbotfarm;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private LinearLayout body;

    private final String[] modules = {
        "Master automation",
        "Gather resources",
        "Prioritize food",
        "Prioritize wood",
        "Prioritize iron",
        "Prioritize mithril",
        "Collect city resources",
        "Alliance help",
        "Alliance donations",
        "Alliance rewards",
        "Train troops",
        "Heal troops",
        "Economic research",
        "Required prerequisite research",
        "Economic buildings",
        "Required prerequisite buildings",
        "Progression planner",
        "Improve marches/load/gathering",
        "Transfer surplus to Main",
        "Maintain reserve",
        "Enforce reinvestment limit",
        "Monitor alliance battles",
        "Protect on attack",
        "Protect-all participation",
        "Recall marches",
        "Auto shield",
        "Routine quests/rewards",
        "Auto account rotation",
        "
