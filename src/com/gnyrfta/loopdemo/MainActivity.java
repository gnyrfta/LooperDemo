/*
	For solving appcompat-v7 problems: 

    Go to your project in the navigator, right click on properties.

    Go to the Java Build Path tab on the left.

    Go to the libraries tab on top.

    Click add external jars.

    Go to your ADT Bundle folder, go to sdk/extras/android/support/v7/appcompat/libs.

    Select the file android-support-v7-appcompat.jar

    Go to order and export and check the box next to your new jar.

    Click ok.
 */

package com.gnyrfta.loopdemo;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidapp.R;

/*
 * Hopefully I can avoid the appcompat problem by always starting my apps by cloning this apptemplate. 
 * But if it would happen, removing v4 from lib folder - all else default when starting application, solved 
 * the problem with duplicate jars this time. 
 */

public class MainActivity extends ActionBarActivity {

	//BPM display:
	TextView bpmView;
	//Sounds added to loop:
	public TextView playListViewOne;
	public TextView playListViewTwo;
	public TextView playListViewThree;
	public TextView playListViewFour;
	public TextView playListViewFive;
	public TextView playListViewSix;
	public TextView playListViewSeven;
	public TextView playListViewEight;

	public Button decreaseDurationButtonOne;
	public Button decreaseDurationButtonTwo;
	public Button decreaseDurationButtonThree;
	public Button decreaseDurationButtonFour;
	public Button decreaseDurationButtonFive;
	public Button decreaseDurationButtonSix;
	public Button decreaseDurationButtonSeven;
	public Button decreaseDurationButtonEight;

	public Button increaseDurationButtonOne;
	public Button increaseDurationButtonTwo;
	public Button increaseDurationButtonThree;
	public Button increaseDurationButtonFour;
	public Button increaseDurationButtonFive;
	public Button increaseDurationButtonSix;
	public Button increaseDurationButtonSeven;
	public Button increaseDurationButtonEight;

	public Button deleteSoundButtonOne;
	public Button deleteSoundButtonTwo;
	public Button deleteSoundButtonThree;
	public Button deleteSoundButtonFour;
	public Button deleteSoundButtonFive;
	public Button deleteSoundButtonSix;
	public Button deleteSoundButtonSeven;
	public Button deleteSoundButtonEight;

	public Thread t;

	public int selected;
	public boolean firstTime=true;

	public int bpm=90;
	public final SoundPoolHelper mSoundPoolHelper= new SoundPoolHelper(2,this);
	//How many milliseconds pass from one sound starts being played until the next sound in the loop comes:
	public final ArrayList<Long> soundLengths = new ArrayList<Long>();
	//List of the sounds as Strings with the name given in the GUI:
	public final ArrayList<String> soundList= new ArrayList<String>();
	//List of the notelengths given to each sound. Along with the bpm this determines the soundLengths:
	public final ArrayList<String> noteList = new ArrayList<String>();
	//this is an arraylist containing ONE boolean - whether or not a change has been made to the loop: 
	final ArrayList<Boolean> changesMade = new ArrayList<Boolean>();
	public final ArrayList<Integer> deleteThis = new ArrayList<Integer>();

	//Ids of the sounds in the form of integers:  
	final ArrayList<Integer> playList = new ArrayList<Integer>();
	final ArrayList<Boolean> playingLoop = new ArrayList<Boolean>();
	final ArrayList<Boolean> clearAll = new ArrayList<Boolean>();
	final ArrayList<Boolean> soundRemoved = new ArrayList<Boolean>();
	final ArrayList<Integer> elementToDelete = new ArrayList<Integer>();
	String sounds="";
	int test;
	int c,d,f,g;
	int claps,cow_bell,cymbal,hihat_closed,hihat_opn,kick_drum,snar_drum,stick_drum;
	long full_note,half_note,quarter_note,eighth_note;
	long currentSoundLength=(long)90/60;
	//DrawOnTop mDraw = new DrawOnTop(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		setContentView(R.layout.activity_main);
		bpmView = (TextView) findViewById(R.id.bpm_text);
		playListViewOne =(TextView) findViewById(R.id.playlist_text_1);
		playListViewTwo = (TextView) findViewById(R.id.playlist_text_2);
		playListViewThree = (TextView) findViewById(R.id.playlist_text_3);
		playListViewFour = (TextView) findViewById(R.id.playlist_text_4);
		playListViewFive = (TextView) findViewById(R.id.playlist_text_5);
		playListViewSix = (TextView) findViewById(R.id.playlist_text_6);
		playListViewSeven = (TextView) findViewById(R.id.playlist_text_7);
		playListViewEight = (TextView) findViewById(R.id.playlist_text_8);

		playListViewOne.setVisibility(View.INVISIBLE);
		playListViewTwo.setVisibility(View.INVISIBLE);
		playListViewThree.setVisibility(View.INVISIBLE);
		playListViewFour.setVisibility(View.INVISIBLE);
		playListViewFive.setVisibility(View.INVISIBLE);
		playListViewSix.setVisibility(View.INVISIBLE);
		playListViewSeven.setVisibility(View.INVISIBLE);
		playListViewEight.setVisibility(View.INVISIBLE);	

		c=mSoundPoolHelper.load(this, R.raw.c,2);
		d=mSoundPoolHelper.load(this, R.raw.d,2);
		f=mSoundPoolHelper.load(this, R.raw.f,2);
		g=mSoundPoolHelper.load(this, R.raw.g,2);
		
		claps=mSoundPoolHelper.load(this, R.raw.claps,2);
		cow_bell=mSoundPoolHelper.load(this, R.raw.cow_bell,2);
		cymbal=mSoundPoolHelper.load(this,R.raw.cymbal,2);
		hihat_closed=mSoundPoolHelper.load(this,R.raw.hihat_closed,2);
		hihat_opn=mSoundPoolHelper.load(this,R.raw.hihat_opn,2);
		kick_drum=mSoundPoolHelper.load(this,R.raw.kick_drum,2);
		snar_drum=mSoundPoolHelper.load(this,R.raw.snar_drum,2);
		stick_drum=mSoundPoolHelper.load(this,R.raw.stick_drum,2);
		//This doesnt't work so well, it assigns the value 1000 to temp1.
		/*double temp0 = (90/60);
		double temp1=temp0*1000;
		long temp2 = Math.round(temp1);
		quarter_note=temp2;
		full_note=quarter_note*4;
		half_note=quarter_note*2;
		double temp3 = temp1/2;
		long temp4 = Math.round(temp3);
		eighth_note=temp4;
		Log.d("quarter_note",""+quarter_note);
		Log.d("temp",""+temp1);*/
		long two = (long)2;
		long four = (long)4;
		double temp = (double)(60/(double)bpm);
		//Log.d("this is temp",""+temp);
		quarter_note=(long)(temp*1000);
		//Log.d("testing123",""+quarter_note);
		half_note=quarter_note*two;
		full_note=quarter_note*four;
		eighth_note=quarter_note/two;

		changesMade.add(false);
		soundRemoved.add(false);
		elementToDelete.add(10);
		clearAll.add(false);

		playingLoop.add(false);

		final Button upperLeftButton =(Button) findViewById(R.id.button_upper_left);
		final Button upperMiddleButton =(Button) findViewById(R.id.button_upper_middle);
		final Button upperRightButton = (Button) findViewById(R.id.button_upper_right);
		final Button lowerLeftButton = (Button) findViewById(R.id.button_lower_left);
		final Button lowerMiddleButton = (Button) findViewById(R.id.button_lower_middle);
		final Button lowerRightButton = (Button) findViewById(R.id.button_lower_right);

		increaseDurationButtonOne = (Button)findViewById(R.id.increaseDuration_1);
		increaseDurationButtonTwo = (Button)findViewById(R.id.increaseDuration_2);
		increaseDurationButtonThree = (Button)findViewById(R.id.increaseDuration_3);
		increaseDurationButtonFour =  (Button)findViewById(R.id.increaseDuration_4);
		increaseDurationButtonFive = (Button)findViewById(R.id.increaseDuration_5);
		increaseDurationButtonSix = (Button)findViewById(R.id.increaseDuration_6);
		increaseDurationButtonSeven = (Button)findViewById(R.id.increaseDuration_7);
		increaseDurationButtonEight =  (Button)findViewById(R.id.increaseDuration_8);

		increaseDurationButtonOne.setVisibility(View.INVISIBLE);
		increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
		increaseDurationButtonThree.setVisibility(View.INVISIBLE);
		increaseDurationButtonFour.setVisibility(View.INVISIBLE);
		increaseDurationButtonFive.setVisibility(View.INVISIBLE);
		increaseDurationButtonSix.setVisibility(View.INVISIBLE);
		increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
		increaseDurationButtonEight.setVisibility(View.INVISIBLE);


		decreaseDurationButtonOne =  (Button)findViewById(R.id.decreaseDuration_1);
		decreaseDurationButtonTwo =  (Button)findViewById(R.id.decreaseDuration_2);
		decreaseDurationButtonThree =  (Button)findViewById(R.id.decreaseDuration_3);
		decreaseDurationButtonFour =  (Button)findViewById(R.id.decreaseDuration_4);
		decreaseDurationButtonFive =  (Button)findViewById(R.id.decreaseDuration_5);
		decreaseDurationButtonSix =  (Button)findViewById(R.id.decreaseDuration_6);
		decreaseDurationButtonSeven =  (Button)findViewById(R.id.decreaseDuration_7);
		decreaseDurationButtonEight =  (Button)findViewById(R.id.decreaseDuration_8);

		decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
		decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
		decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
		decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
		decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
		decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
		decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
		decreaseDurationButtonEight.setVisibility(View.INVISIBLE);

		deleteSoundButtonOne = (Button)findViewById(R.id.deleteSound_1);
		deleteSoundButtonTwo = (Button)findViewById(R.id.deleteSound_2);
		deleteSoundButtonThree = (Button)findViewById(R.id.deleteSound_3);
		deleteSoundButtonFour = (Button)findViewById(R.id.deleteSound_4);
		deleteSoundButtonFive = (Button)findViewById(R.id.deleteSound_5);
		deleteSoundButtonSix = (Button)findViewById(R.id.deleteSound_6);
		deleteSoundButtonSeven = (Button)findViewById(R.id.deleteSound_7);
		deleteSoundButtonEight = (Button)findViewById(R.id.deleteSound_8);

		deleteSoundButtonOne.setVisibility(View.INVISIBLE);
		deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
		deleteSoundButtonThree.setVisibility(View.INVISIBLE);
		deleteSoundButtonFour.setVisibility(View.INVISIBLE);
		deleteSoundButtonFive.setVisibility(View.INVISIBLE);
		deleteSoundButtonSix.setVisibility(View.INVISIBLE);
		deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
		deleteSoundButtonEight.setVisibility(View.INVISIBLE);

		final RadioGroup  radiogroup = (RadioGroup) findViewById(R.id.radio_note_length);
		final RadioButton radioButtonWhole = (RadioButton) findViewById(R.id.whole_note);
		final RadioButton radioButtonHalf = (RadioButton) findViewById(R.id.half_note);
		final RadioButton radioButtonQuarter = (RadioButton) findViewById(R.id.quarter_note);
		final RadioButton radioButtonEighth = (RadioButton) findViewById(R.id.eighth_note);

		radioButtonWhole.setId(1);
		radioButtonHalf.setId(2);
		radioButtonQuarter.setId(4);
		radioButtonEighth.setId(8);
		radioButtonQuarter.setChecked(true);



		selected = 4;

		radiogroup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int selectedId = radiogroup.getCheckedRadioButtonId();
				currentSoundLength=setCurrentSoundLength(selectedId);
			}
		});

		final Spinner spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setSelection(1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.sounds_array, android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		//
		//addContentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//mDraw.bringToFront();
		//
		//"Actions"
		//
		upperLeftButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bpm+=5;
				setBPM(bpm);
			}
		});
		upperMiddleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selected = radiogroup.getCheckedRadioButtonId();
				//This is a workaround since I don't understand why selected initially 
				//doesn't have the value 4. 
				if(firstTime)
				{
					selected=4;
					firstTime=false;
				}
				String s = spinner.getSelectedItem().toString();
				if(playList.size()<8)
				{
					addSound(selected, s);
					Log.d("selected: ",""+selected);
				}
				if(playList.size()==1)
				{
					playListViewOne.setVisibility(View.VISIBLE);
					playListViewOne.setText(soundList.get(0));
					increaseDurationButtonOne.setVisibility(View.VISIBLE);
					decreaseDurationButtonOne.setVisibility(View.VISIBLE);
					deleteSoundButtonOne.setVisibility(View.VISIBLE);
				}
				if(playList.size()==2)
				{
					playListViewTwo.setVisibility(View.VISIBLE);
					playListViewTwo.setText(soundList.get(1));
					increaseDurationButtonTwo.setVisibility(View.VISIBLE);
					decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
					deleteSoundButtonTwo.setVisibility(View.VISIBLE);
				}
				if(playList.size()==3)
				{
					playListViewThree.setVisibility(View.VISIBLE);
					playListViewThree.setText(soundList.get(2));
					increaseDurationButtonThree.setVisibility(View.VISIBLE);
					decreaseDurationButtonThree.setVisibility(View.VISIBLE);
					deleteSoundButtonThree.setVisibility(View.VISIBLE);
				}
				if(playList.size()==4)
				{
					playListViewFour.setVisibility(View.VISIBLE);
					playListViewFour.setText(soundList.get(3));
					increaseDurationButtonFour.setVisibility(View.VISIBLE);
					decreaseDurationButtonFour.setVisibility(View.VISIBLE);
					deleteSoundButtonFour.setVisibility(View.VISIBLE);
				}
				if(playList.size()==5)
				{
					playListViewFive.setVisibility(View.VISIBLE);
					playListViewFive.setText(soundList.get(4));
					increaseDurationButtonFive.setVisibility(View.VISIBLE);
					decreaseDurationButtonFive.setVisibility(View.VISIBLE);
					deleteSoundButtonFive.setVisibility(View.VISIBLE);
				}
				if(playList.size()==6)
				{
					playListViewSix.setVisibility(View.VISIBLE);
					playListViewSix.setText(soundList.get(5));
					increaseDurationButtonSix.setVisibility(View.VISIBLE);
					decreaseDurationButtonSix.setVisibility(View.VISIBLE);
					deleteSoundButtonSix.setVisibility(View.VISIBLE);
				}
				if(playList.size()==7)
				{
					playListViewSeven.setVisibility(View.VISIBLE);
					playListViewSeven.setText(soundList.get(6));
					increaseDurationButtonSeven.setVisibility(View.VISIBLE);
					decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
					deleteSoundButtonSeven.setVisibility(View.VISIBLE);
				}
				if(playList.size()==8)
				{
					playListViewEight.setVisibility(View.VISIBLE);
					playListViewEight.setText(soundList.get(7));
					increaseDurationButtonEight.setVisibility(View.VISIBLE);
					decreaseDurationButtonEight.setVisibility(View.VISIBLE);
					deleteSoundButtonEight.setVisibility(View.VISIBLE);
				}

			}
		});	
		upperRightButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!playingLoop.get(0))
				{
				playingLoop.set(0,true);
				play();
				}
			}
		});
		//
		lowerLeftButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bpm-=5;
				setBPM(bpm);
			}
		});
		lowerMiddleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//clearLists();
				//make function clear() and set text for button.
			}
		});
		lowerRightButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stop();
			}
		});
		//
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {
				//String selected = spinner.getSelectedItem().toString();// your code here
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});
		//
		increaseDurationButtonOne.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(1);


			}
		});
		increaseDurationButtonTwo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(2);


			}
		});
		increaseDurationButtonThree.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(3);
			}
		});
		increaseDurationButtonFour.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(4);
			}
		});
		increaseDurationButtonFive.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(5);
			}
		});
		increaseDurationButtonSix.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(6);
			}
		});
		increaseDurationButtonSeven.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(7);
			}
		});
		increaseDurationButtonEight.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				increaseDuration(8);
			}
		});
		//
		decreaseDurationButtonOne.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(1);
			}
		});
		decreaseDurationButtonTwo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(2);
			}
		});	
		decreaseDurationButtonThree.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(3);
			}
		});
		decreaseDurationButtonFour.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(4);
			}
		});
		decreaseDurationButtonFive.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(5);
			}
		});
		decreaseDurationButtonSix.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(6);
			}
		});
		decreaseDurationButtonSeven.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(7);
			}
		});
		decreaseDurationButtonEight.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				decreaseDuration(8);
			}
		});
		deleteSoundButtonOne.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				/*This button is now slightly different than the others, but don't want
				 * to change the others until i've figured out something that works.*/

				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				Log.d("this is playingLoop value",""+playingLoop.get(0));
				int amountOfSounds=soundLengths.size();
				int amountOfSoundsBeforeDeletion=soundLengths.size();
				if(playingLoop.get(0))
				{	
					Log.d("in initial if-loop","in initial if-place");
					deleteThis.clear();
					deleteThis.add(0);
					//t.setPriority(Thread.MAX_PRIORITY);
					changesMade.set(0,true);
					try
					{
						Thread.sleep(2);
					}catch(Exception e){}

					/*while(amountOfSounds==soundLengths.size())
					{
						//Do nothing
						//try{
						//Thread.sleep(1);
						//}
						//catch(Exception e){}
					}
					amountOfSounds=soundLengths.size();*/
				}
				else
				{
					int temp=0;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				updatePlayListGUI(amountOfSoundsBeforeDeletion);
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 

				/*if(!playingLoop.get(0))
				{
					for(int i=0;i<amountOfSounds;i++)
					{
						if(i==0)
						{
							deleteSoundButtonOne.setVisibility(View.VISIBLE);
							increaseDurationButtonOne.setVisibility(View.VISIBLE);
							decreaseDurationButtonOne.setVisibility(View.VISIBLE);
							playListViewOne.setVisibility(View.VISIBLE);			
							playListViewOne.setText(soundList.get(0));
						}
						if(i==1)
						{
							deleteSoundButtonTwo.setVisibility(View.VISIBLE);
							increaseDurationButtonTwo.setVisibility(View.VISIBLE);
							decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
							playListViewTwo.setVisibility(View.VISIBLE);
							playListViewTwo.setText(soundList.get(1));
						}
						if(i==2)
						{
							deleteSoundButtonThree.setVisibility(View.VISIBLE);
							increaseDurationButtonThree.setVisibility(View.VISIBLE);
							decreaseDurationButtonThree.setVisibility(View.VISIBLE);
							playListViewThree.setVisibility(View.VISIBLE);			
							playListViewThree.setText(soundList.get(2));
						}
						if(i==3)
						{
							deleteSoundButtonFour.setVisibility(View.VISIBLE);
							increaseDurationButtonFour.setVisibility(View.VISIBLE);
							decreaseDurationButtonFour.setVisibility(View.VISIBLE);
							playListViewFour.setVisibility(View.VISIBLE);
							playListViewFour.setText(soundList.get(3));
						}
						if(i==4)
						{
							deleteSoundButtonFive.setVisibility(View.VISIBLE);
							increaseDurationButtonFive.setVisibility(View.VISIBLE);
							decreaseDurationButtonFive.setVisibility(View.VISIBLE);
							playListViewFive.setVisibility(View.VISIBLE);
							playListViewFive.setText(soundList.get(4));
						}
						if(i==5)
						{
							deleteSoundButtonSix.setVisibility(View.VISIBLE);
							increaseDurationButtonSix.setVisibility(View.VISIBLE);
							decreaseDurationButtonSix.setVisibility(View.VISIBLE);
							playListViewSix.setVisibility(View.VISIBLE);
							playListViewSix.setText(soundList.get(5));
						}
						if(i==6)
						{
							deleteSoundButtonSeven.setVisibility(View.VISIBLE);
							increaseDurationButtonSeven.setVisibility(View.VISIBLE);
							decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
							playListViewSeven.setVisibility(View.VISIBLE);		
							playListViewSeven.setText(soundList.get(6));
						}
						if(i==7)
						{
							deleteSoundButtonEight.setVisibility(View.VISIBLE);
							increaseDurationButtonEight.setVisibility(View.VISIBLE);
							decreaseDurationButtonEight.setVisibility(View.VISIBLE);
							playListViewEight.setVisibility(View.VISIBLE);
							playListViewEight.setText(soundList.get(7));
						}
					}
					for(int i=amountOfSounds; i<8;i++)
					{
						if(i==0)
						{
							deleteSoundButtonOne.setVisibility(View.INVISIBLE);
							increaseDurationButtonOne.setVisibility(View.INVISIBLE);
							decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
							playListViewOne.setVisibility(View.INVISIBLE);	
						}
						if(i==1)
						{
							deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
							increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
							decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
							playListViewTwo.setVisibility(View.INVISIBLE);						
						}
						if(i==2)
						{
							deleteSoundButtonThree.setVisibility(View.INVISIBLE);
							increaseDurationButtonThree.setVisibility(View.INVISIBLE);
							decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
							playListViewThree.setVisibility(View.INVISIBLE);						
						}
						if(i==3)
						{
							deleteSoundButtonFour.setVisibility(View.INVISIBLE);
							increaseDurationButtonFour.setVisibility(View.INVISIBLE);
							decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
							playListViewFour.setVisibility(View.INVISIBLE);						
						}
						if(i==4)
						{
							deleteSoundButtonFive.setVisibility(View.INVISIBLE);
							increaseDurationButtonFive.setVisibility(View.INVISIBLE);
							decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
							playListViewFive.setVisibility(View.INVISIBLE);						
						}
						if(i==5)
						{
							deleteSoundButtonSix.setVisibility(View.INVISIBLE);
							increaseDurationButtonSix.setVisibility(View.INVISIBLE);
							decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
							playListViewSix.setVisibility(View.INVISIBLE);						
						}
						if(i==6)
						{
							deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
							increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
							decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
							playListViewSeven.setVisibility(View.INVISIBLE);						
						}
						if(i==7)
						{
							deleteSoundButtonEight.setVisibility(View.INVISIBLE);
							increaseDurationButtonEight.setVisibility(View.INVISIBLE);
							decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
							playListViewEight.setVisibility(View.INVISIBLE);						
						}
					}
					/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));

				}*/
			}
		});
		deleteSoundButtonTwo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{	
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(1);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=1;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});

		deleteSoundButtonThree.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{	
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(2);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=2;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
		deleteSoundButtonFour.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(3);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}
					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=3;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
		deleteSoundButtonFive.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(4);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=4;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
		deleteSoundButtonSix.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(5);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=5;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
		deleteSoundButtonSeven.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(6);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=6;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
		deleteSoundButtonEight.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.d("deleteSoundButtonPressed","deleteSoundButtonPressed");
				int amountOfSounds;
				if(playingLoop.get(0))
				{
					amountOfSounds=soundLengths.size();
					deleteThis.clear();
					deleteThis.add(7);
					changesMade.set(0,true);
					while(amountOfSounds==soundLengths.size())
					{
						try{
							Thread.sleep(1);
						}
						catch(Exception e){}

					}
					amountOfSounds=soundLengths.size();
				}
				else
				{
					int temp=7;
					soundLengths.remove(temp);
					playList.remove(temp);
					soundList.remove(temp);
					noteList.remove(temp);
					amountOfSounds=soundLengths.size();
				}
				//Need separate deletion for if loop is closed.
				//Need to make a function for textviews to shift downwards, or reassign rather.
				//					playListViewSeven.setText(soundList.get(6)); et.c.
				//possibly try making changes directly here and then writing them. 
				for(int i=0;i<amountOfSounds;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.VISIBLE);
						increaseDurationButtonOne.setVisibility(View.VISIBLE);
						decreaseDurationButtonOne.setVisibility(View.VISIBLE);
						playListViewOne.setVisibility(View.VISIBLE);			
						playListViewOne.setText(soundList.get(0));
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.VISIBLE);
						increaseDurationButtonTwo.setVisibility(View.VISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setVisibility(View.VISIBLE);
						playListViewTwo.setText(soundList.get(1));
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.VISIBLE);
						increaseDurationButtonThree.setVisibility(View.VISIBLE);
						decreaseDurationButtonThree.setVisibility(View.VISIBLE);
						playListViewThree.setVisibility(View.VISIBLE);			
						playListViewThree.setText(soundList.get(2));
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.VISIBLE);
						increaseDurationButtonFour.setVisibility(View.VISIBLE);
						decreaseDurationButtonFour.setVisibility(View.VISIBLE);
						playListViewFour.setVisibility(View.VISIBLE);
						playListViewFour.setText(soundList.get(3));
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.VISIBLE);
						increaseDurationButtonFive.setVisibility(View.VISIBLE);
						decreaseDurationButtonFive.setVisibility(View.VISIBLE);
						playListViewFive.setVisibility(View.VISIBLE);
						playListViewFive.setText(soundList.get(4));
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.VISIBLE);
						increaseDurationButtonSix.setVisibility(View.VISIBLE);
						decreaseDurationButtonSix.setVisibility(View.VISIBLE);
						playListViewSix.setVisibility(View.VISIBLE);
						playListViewSix.setText(soundList.get(5));
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.VISIBLE);
						increaseDurationButtonSeven.setVisibility(View.VISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
						playListViewSeven.setVisibility(View.VISIBLE);		
						playListViewSeven.setText(soundList.get(6));
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.VISIBLE);
						increaseDurationButtonEight.setVisibility(View.VISIBLE);
						decreaseDurationButtonEight.setVisibility(View.VISIBLE);
						playListViewEight.setVisibility(View.VISIBLE);
						playListViewEight.setText(soundList.get(7));
					}
				}
				for(int i=amountOfSounds; i<8;i++)
				{
					if(i==0)
					{
						deleteSoundButtonOne.setVisibility(View.INVISIBLE);
						increaseDurationButtonOne.setVisibility(View.INVISIBLE);
						decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
						playListViewOne.setVisibility(View.INVISIBLE);	
					}
					if(i==1)
					{
						deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
						increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
						playListViewTwo.setVisibility(View.INVISIBLE);						
					}
					if(i==2)
					{
						deleteSoundButtonThree.setVisibility(View.INVISIBLE);
						increaseDurationButtonThree.setVisibility(View.INVISIBLE);
						decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
						playListViewThree.setVisibility(View.INVISIBLE);						
					}
					if(i==3)
					{
						deleteSoundButtonFour.setVisibility(View.INVISIBLE);
						increaseDurationButtonFour.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
						playListViewFour.setVisibility(View.INVISIBLE);						
					}
					if(i==4)
					{
						deleteSoundButtonFive.setVisibility(View.INVISIBLE);
						increaseDurationButtonFive.setVisibility(View.INVISIBLE);
						decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
						playListViewFive.setVisibility(View.INVISIBLE);						
					}
					if(i==5)
					{
						deleteSoundButtonSix.setVisibility(View.INVISIBLE);
						increaseDurationButtonSix.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
						playListViewSix.setVisibility(View.INVISIBLE);						
					}
					if(i==6)
					{
						deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
						increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
						playListViewSeven.setVisibility(View.INVISIBLE);						
					}
					if(i==7)
					{
						deleteSoundButtonEight.setVisibility(View.INVISIBLE);
						increaseDurationButtonEight.setVisibility(View.INVISIBLE);
						decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
						playListViewEight.setVisibility(View.INVISIBLE);						
					}
				}
				/*changesMade.clear();
				changesMade.add(true);
				soundRemoved.clear();
				soundRemoved.add(true);
				elementToDelete.clear();
				elementToDelete.add(0);
				Log.d("this is changesMade",""+changesMade.get(0));
				 */
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//Do things
			//	tv.setText("Helllooooo world!!!");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public ArrayList<String> addSound(String sound, ArrayList<String> soundList)
	{
		ArrayList<String> test = soundList;
		test.add(sound);
		return test;
	}
	public void loadSounds(ArrayList<String> soundList)
	{
		//ArrayList<Integer> ids = new ArrayList<Integer>();
		playList.clear();
		for(int i=0;i<soundList.size();i++)
		{
			String s=soundList.get(i);

			if(s.equals("Piano C"))
			{
				//ids.add(c);
				playList.add(c);
				Log.d("added c","added c");
			}
			if(s.equals("Piano D"))
			{
				//ids.add(d);		
				playList.add(d);
				Log.d("added d","added d");

			}
			if(s.equals("Piano F"))
			{
				//ids.add(f);
				playList.add(f);
				Log.d("added f","added f");

			}
			if(s.equals("Piano G"))
			{
				//ids.add(g);
				playList.add(g);
				Log.d("added g","added g");

			}
			if(s.equals("claps"))
			{
				playList.add(claps);
				Log.d("added claps","added claps");
			}
			if(s.equals("cowbell"))
			{
				playList.add(cow_bell);
				Log.d("added cowbell","added cowbell");
			}
			if(s.equals("cymbal"))
			{
				playList.add(cymbal);
				Log.d("added cymbal","added cymbal");
			}
			if(s.equals("hi-hat closed"))
			{
				playList.add(hihat_closed);
				Log.d("added closed hihat","added closed hihat");
			}
			if(s.equals("hi-hat open"))
			{
				playList.add(hihat_opn);
				Log.d("added open hihat","added open hihat");
			}
			if(s.equals("kick drum"))
			{
				playList.add(kick_drum);
				Log.d("added kick drum","added kick drum");
			}
			if(s.equals("snare drum"))
			{
				playList.add(snar_drum);
				Log.d("added snare drum","added snare drum");
			}
			if(s.equals("stick drum"))
			{
				playList.add(stick_drum);
				Log.d("added stick drum","added stick drum");
			}
		}
		Log.d("exiting loading of sounds","exiting loading of sounds");
		//return ids;
	}
	public void Looper(ArrayList<Integer> soundList, ArrayList<Long> timeIntervalls,int BPM)
	{
		long startTime = System.nanoTime();
		//long oneSecond = (long)1000;
		long timePassedMilliSeconds=0;
		boolean firstTime=true;
		//Andante 72 bpm -> 60/72 s per not.
		//Detta är alltså typ långsam promenad.
		//ArrayList<Integer> sounds = getSounds();
		//ArrayList<Long> TimeIntervalls = getTimeIntervalls();
		//mSoundPoolHelper.play(test);

		Log.d("entering while","entering while");
		while(true)
		{		
			//int test=mSoundPoolHelper.load(this, R.raw.c,2);
			//mSoundPoolHelper.play(test);			
			for(int i=0;i<soundList.size();i++)
			{
				while((timePassedMilliSeconds)<timeIntervalls.get(i))
				{		
					if(firstTime)
					{	
						mSoundPoolHelper.play(soundList.get(i));
						firstTime=false;
						Log.d("should have played first sound in list","");
					}
					long timePassed=System.nanoTime()-startTime;
					timePassedMilliSeconds = timePassed/1000000;
					Log.d("time passed"," "+timePassedMilliSeconds);
				}
				startTime=System.nanoTime();
				firstTime=true;
				timePassedMilliSeconds=0;
			}
		}	
	}
	private ArrayList<Long> setNoteLengths(int bpm) {
		//noteList.clear();
		long two = (long)2;
		long four = (long)4;
		double temp = (double)(60/(double)bpm);
		Log.d("this is temp",""+temp);
		long quarter_note=(long)(temp*1000);
		Log.d("testing123",""+quarter_note);
		long half_note=quarter_note*two;
		long full_note=quarter_note*four;
		long eighth_note=quarter_note/two;
		ArrayList<Long> returnList = new ArrayList<Long>();
		returnList.add(full_note);
		returnList.add(half_note);
		returnList.add(quarter_note);
		returnList.add(eighth_note);
		/*noteList.add(full_note);
		noteList.add(half_note);
		noteList.add(quarter_note);
		noteList.add(eighth_note);*/
		Log.d("this is full_note in setNoteLengths:",""+returnList.get(0));
		return returnList;
	}
	private void updateDurations(ArrayList<String> notes) {
		//soundLengths.clear();
		ArrayList<Long> noteLengths = setNoteLengths(bpm);

		for(int i=0;i<notes.size();i++)
		{
			Log.d("note",""+notes.get(i));
			//Log.d("noteLength",""+noteLengths.get(i));

			if(notes.get(i).equals("full_note"))
			{
				//soundLengths.add(noteLengths.get(0));
				soundLengths.set(i,noteLengths.get(0));
			}
			if(notes.get(i).equals("half_note"))
			{
				soundLengths.set(i,noteLengths.get(1));
			}
			if(notes.get(i).equals("quarter_note"))
			{
				soundLengths.set(i,noteLengths.get(2));
			}
			if(notes.get(i).equals("eighth_note"))
			{
				soundLengths.set(i,noteLengths.get(3));
			}
		}
		Log.d("in updateDurations, this is soundLengths.size",""+soundLengths.size());
		Log.d("in updateDurations, this is noteLengths.size",""+noteLengths.size());
		Log.d("in updateDurations, this is notes.size",""+notes.size());
	}
	public long setCurrentSoundLength(int selectedId)
	{
		//make function set currentSoundLength.
		// get selected radio button from radioGroup
		//set note lengths to appropriate values: 
		ArrayList<Long> noteLengths = setNoteLengths(bpm);
		full_note=noteLengths.get(0);
		half_note=noteLengths.get(1);
		quarter_note=noteLengths.get(2);
		eighth_note=noteLengths.get(3);
		Log.d("quarter_note",""+quarter_note);


		switch(selectedId)
		{
		case 1: currentSoundLength=full_note;
		break; 
		case 2: currentSoundLength=half_note;
		break;
		case 4: currentSoundLength=quarter_note;
		break;
		case 8: currentSoundLength=eighth_note;
		break;
		}
		// find the radiobutton by returned id
		return currentSoundLength;
	}
	public void addSound(int selected,String s)
	{
		//Need an arraylist to check how many sounds there are already. 
		//then a switch case for making the right set of textviews and buttons visible. 
		setNoteLengths(bpm);
		String note = "";
		Log.d("selected",""+selected);
		//This is not correct - the sound lengths should be set when the loop starts. 
		switch(selected)
		{
		case 1: currentSoundLength=full_note;
		note="full_note";
		break; 
		case 2: currentSoundLength=half_note;
		note="half_note";
		break;
		case 4: currentSoundLength=quarter_note;
		note="quarter_note";
		break;
		case 8: currentSoundLength=eighth_note;
		note="eighth_note";
		break;
		}
		soundList.add(s);
		loadSounds(soundList);
		//soundList=addSound(s,soundList);
		Log.d("currentSoundLength",""+currentSoundLength);
		soundLengths.add(currentSoundLength);
		//Need to fix this, then need to take away unused variables, mark out globals and try to get 
		//rid of inner methods for readability. 
		noteList.add(note);
		changesMade.set(0,true);
		//soundLengths=addDuration(currentSoundLength,soundLengths);
		//Intent i = new Intent(MainActivity.this,Pad.class);
		//i.putExtra("ULk", ULk);
		//i.putExtra("URk",URk);
		//i.putExtra("LLk",LLk);
		//i.putExtra("LRk",LRk);
		//startActivity(i);
	}
	public void play()
	{
		//make function play()
		//gets added sounds:
		loadSounds(soundList);
		//sets the notelengths according to bpm:
		//setNoteLengths(bpm);

		updateDurations(noteList);// should be here? 

		t = new Thread() 
		{

			public void run()
			{
				ArrayList<Long> timeIntervalls=soundLengths;
				ArrayList<Integer> soundList2 = playList;
				int takeAwayThis = elementToDelete.get(0);

				long startTime = System.nanoTime();
				//long oneSecond = (long)1000;
				long timePassedMilliSeconds=0;
				boolean firstTime=true;
				//Andante 72 bpm -> 60/72 s per not.
				//Detta är alltså typ långsam promenad.
				//ArrayList<Integer> sounds = getSounds();
				//ArrayList<Long> TimeIntervalls = getTimeIntervalls();
				//mSoundPoolHelper.play(test);

				Log.d("entering while","entering while");
				while(playingLoop.get(0))
				{		

					if(changesMade.get(0))
					{
						Log.d("this is soundLength just before problem 1",""+soundLengths.size());
						Log.d("this is playList size just before problem 1",""+playList.size());
						Log.d("this is deleteThis.size",""+deleteThis.size());
						if(deleteThis.size()>0)
						{
							int temp=deleteThis.get(0);
							soundLengths.remove(temp);
							playList.remove(temp);
							soundList.remove(temp);
							noteList.remove(temp);
							deleteThis.clear();
							/*try{								
								Thread.sleep(3);
							}catch(Exception e){}*/
							Log.d("this is soundLength just before problem 2",""+soundLengths.size());
							Log.d("this is playList size just before problem 2",""+playList.size());	
						}
						timeIntervalls=soundLengths;
						soundList2 = playList;
						try{
							Thread.sleep(5);
						}catch(Exception e){}
						//if(variable==2): remove element two from arraylists
						//et.c. write as function. then reset variable. 						
						changesMade.clear();
						changesMade.add(false);
						//	updatePlayListGUI();
					}
					//int test=mSoundPoolHelper.load(this, R.raw.c,2);
					//mSoundPoolHelper.play(test);			
					for(int i=0;i<soundList2.size();i++)
					{
						if(soundList2.size()==timeIntervalls.size())
						{

							while((timePassedMilliSeconds)<timeIntervalls.get(i))
							{		
								if(firstTime)
								{	
									mSoundPoolHelper.play(soundList2.get(i));
									firstTime=false;
									Log.d("should have played first sound in list","");
								}
								long timePassed=System.nanoTime()-startTime;
								timePassedMilliSeconds = timePassed/1000000;
								if(changesMade.get(0))
								{
									if(i==timeIntervalls.size()-1)
									{
										Log.d("this is soundLength just before problem 1",""+soundLengths.size());
										Log.d("this is playList size just before problem 1",""+playList.size());
										Log.d("this is deleteThis.size",""+deleteThis.size());
										if(deleteThis.size()>0)
										{
											int temp=deleteThis.get(0);
											soundLengths.remove(temp);
											playList.remove(temp);
											soundList.remove(temp);
											noteList.remove(temp);
											deleteThis.clear();
											/*try{								
												Thread.sleep(3);
											}catch(Exception e){}*/
											Log.d("this is soundLength just before problem 2",""+soundLengths.size());
											Log.d("this is playList size just before problem 2",""+playList.size());	
										}
										timeIntervalls=soundLengths;
										soundList2 = playList;
										try{
											Thread.sleep(5);
										}catch(Exception e){}
										//if(variable==2): remove element two from arraylists
										//et.c. write as function. then reset variable. 						
										changesMade.clear();
										changesMade.add(false);
										break;
									}
									else
									{
										Log.d("this is soundLength just before problem 1",""+soundLengths.size());
										Log.d("this is playList size just before problem 1",""+playList.size());
										Log.d("this is deleteThis.size",""+deleteThis.size());
										if(deleteThis.size()>0)
										{
											int temp=deleteThis.get(0);
											soundLengths.remove(temp);
											playList.remove(temp);
											soundList.remove(temp);
											noteList.remove(temp);
											deleteThis.clear();
											/*try{								
												Thread.sleep(3);
											}catch(Exception e){}*/
											Log.d("this is soundLength just before problem 2",""+soundLengths.size());
											Log.d("this is playList size just before problem 2",""+playList.size());	
										}
										timeIntervalls=soundLengths;
										soundList2 = playList;
										try{
											Thread.sleep(5);
										}catch(Exception e){}
										//if(variable==2): remove element two from arraylists
										//et.c. write as function. then reset variable. 						
										changesMade.clear();
										changesMade.add(false);
									}
								}
								//	Log.d("time passed"," "+timePassedMilliSeconds);
							}
							startTime=System.nanoTime();
							firstTime=true;
							timePassedMilliSeconds=0;
						}
						else
						{
							//do nothing. Catching possible synching problems between arraylists. 
						}
					}	
				}	
			}
		};
		t.start();

		//Looper(playList,soundLengths,bpm);

		//Intent i = new Intent(MainActivity.this,Pad.class);
		//i.putExtra("ULk", ULk);
		//i.putExtra("URk",URk);
		//i.putExtra("LLk",LLk);
		//i.putExtra("LRk",LRk);
		//startActivity(i);
	}
	public void setBPM(int bpm)
	{
		bpm+=5;
		bpmView.setText("BPM: "+bpm);
		setNoteLengths(bpm);
		updateDurations(noteList);
		changesMade.set(0,true);
	}
	public void clearList()
	{
		//changesMade.clear();
		//changesMade.add(true);
	}
	public void run2()
	{
		ArrayList<Long> timeIntervalls=soundLengths;
		ArrayList<Integer> soundList = playList;
		for(int i=0;i<soundList.size();i++)
		{
			Log.d("this is sound",""+soundList.get(i));
			Log.d("this is duration in ms:",""+timeIntervalls.get(i));
		}
		long startTime = System.nanoTime();
		//long oneSecond = (long)1000;
		long timePassedMilliSeconds=0;
		boolean firstTime=true;
		//Andante 72 bpm -> 60/72 s per not.
		//Detta är alltså typ långsam promenad.
		//ArrayList<Integer> sounds = getSounds();
		//ArrayList<Long> TimeIntervalls = getTimeIntervalls();
		//mSoundPoolHelper.play(test);

		Log.d("entering while","entering while");
		while(true)
		{		
			if(changesMade.get(0))
			{

				changesMade.clear();
				changesMade.add(false);
				timeIntervalls=soundLengths;
				soundList = playList;
			}
			//int test=mSoundPoolHelper.load(this, R.raw.c,2);
			//mSoundPoolHelper.play(test);			
			for(int i=0;i<soundList.size();i++)
			{
				Log.d("this is soundLength just before problem",""+timeIntervalls.size());
				while((timePassedMilliSeconds)<timeIntervalls.get(i))
				{		
					if(firstTime)
					{	
						updateGraphics();
						/* mSoundPoolHelper.play(soundList.get(i));*/
						firstTime=false;
						Log.d("should have played first sound in list","");
					}
					long timePassed=System.nanoTime()-startTime;
					timePassedMilliSeconds = timePassed/1000000;
					Log.d("time passed"," "+timePassedMilliSeconds);
				}
				startTime=System.nanoTime();
				firstTime=true;
				timePassedMilliSeconds=0;
			}
		}	
	}
	public void updateGraphics()
	{ 	//if first time or something changed:
		//first sound furthest to the right
		//spacing proportional to duration of this sound
		//then next sound. 
		//the drawn sound should be an object that is selectable
		//mDraw.setDrawingVariables(soundLengths, soundList);
		//mDraw.invalidate();
	}
	public void stop()
	{
		playingLoop.set(0,false);
	}
	public void clearLists()
	{	
		soundList.clear();
		changesMade.clear();
		changesMade.add(true);
		clearAll.clear();
		clearAll.add(true);
		sounds="";
		playListViewOne.setText(sounds);
	}
	public void increaseDuration(int placementInPlayList)
	{
		int temp = placementInPlayList-1;//adjusting for index starting at zero.
		long currentDuration = soundLengths.get(temp);
		double temp2 = (double)eighth_note;
		double temp3 = temp2/4;
		long temp4 = Math.round(temp3);
		long temp5 = currentDuration+temp4;//increasing by 1/32 note.
		soundLengths.set(placementInPlayList-1,temp5);
	}
	public void decreaseDuration(int placementInPlayList)
	{
		int temp = placementInPlayList-1;//adjusting for index starting at zero.
		long currentDuration = soundLengths.get(temp);
		double temp2 = (double)eighth_note;
		double temp3 = temp2/4;
		long temp4 = Math.round(temp3);
		long temp5 = currentDuration-temp4;//increasing by 1/32 note.
		soundLengths.set(placementInPlayList-1,temp5);
	}
	public void updatePlayListGUI(int sizeBeforeChange)
	{
		int amountOfSounds=sizeBeforeChange-1;
		for(int i=0;i<amountOfSounds;i++)
		{
			if(i==0)
			{
				deleteSoundButtonOne.setVisibility(View.VISIBLE);
				increaseDurationButtonOne.setVisibility(View.VISIBLE);
				decreaseDurationButtonOne.setVisibility(View.VISIBLE);
				playListViewOne.setVisibility(View.VISIBLE);			
				playListViewOne.setText(soundList.get(0));
			}
			if(i==1)
			{
				deleteSoundButtonTwo.setVisibility(View.VISIBLE);
				increaseDurationButtonTwo.setVisibility(View.VISIBLE);
				decreaseDurationButtonTwo.setVisibility(View.VISIBLE);
				playListViewTwo.setVisibility(View.VISIBLE);
				playListViewTwo.setText(soundList.get(1));
			}
			if(i==2)
			{
				deleteSoundButtonThree.setVisibility(View.VISIBLE);
				increaseDurationButtonThree.setVisibility(View.VISIBLE);
				decreaseDurationButtonThree.setVisibility(View.VISIBLE);
				playListViewThree.setVisibility(View.VISIBLE);			
				playListViewThree.setText(soundList.get(2));
			}
			if(i==3)
			{
				deleteSoundButtonFour.setVisibility(View.VISIBLE);
				increaseDurationButtonFour.setVisibility(View.VISIBLE);
				decreaseDurationButtonFour.setVisibility(View.VISIBLE);
				playListViewFour.setVisibility(View.VISIBLE);
				playListViewFour.setText(soundList.get(3));
			}
			if(i==4)
			{
				deleteSoundButtonFive.setVisibility(View.VISIBLE);
				increaseDurationButtonFive.setVisibility(View.VISIBLE);
				decreaseDurationButtonFive.setVisibility(View.VISIBLE);
				playListViewFive.setVisibility(View.VISIBLE);
				playListViewFive.setText(soundList.get(4));
			}
			if(i==5)
			{
				deleteSoundButtonSix.setVisibility(View.VISIBLE);
				increaseDurationButtonSix.setVisibility(View.VISIBLE);
				decreaseDurationButtonSix.setVisibility(View.VISIBLE);
				playListViewSix.setVisibility(View.VISIBLE);
				playListViewSix.setText(soundList.get(5));
			}
			if(i==6)
			{
				deleteSoundButtonSeven.setVisibility(View.VISIBLE);
				increaseDurationButtonSeven.setVisibility(View.VISIBLE);
				decreaseDurationButtonSeven.setVisibility(View.VISIBLE);
				playListViewSeven.setVisibility(View.VISIBLE);		
				playListViewSeven.setText(soundList.get(6));
			}
			if(i==7)
			{
				deleteSoundButtonEight.setVisibility(View.VISIBLE);
				increaseDurationButtonEight.setVisibility(View.VISIBLE);
				decreaseDurationButtonEight.setVisibility(View.VISIBLE);
				playListViewEight.setVisibility(View.VISIBLE);
				playListViewEight.setText(soundList.get(7));
			}
		}
		for(int i=amountOfSounds; i<8;i++)
		{
			if(i==0)
			{
				deleteSoundButtonOne.setVisibility(View.INVISIBLE);
				increaseDurationButtonOne.setVisibility(View.INVISIBLE);
				decreaseDurationButtonOne.setVisibility(View.INVISIBLE);
				playListViewOne.setVisibility(View.INVISIBLE);	
			}
			if(i==1)
			{
				deleteSoundButtonTwo.setVisibility(View.INVISIBLE);
				increaseDurationButtonTwo.setVisibility(View.INVISIBLE);
				decreaseDurationButtonTwo.setVisibility(View.INVISIBLE);
				playListViewTwo.setVisibility(View.INVISIBLE);						
			}
			if(i==2)
			{
				deleteSoundButtonThree.setVisibility(View.INVISIBLE);
				increaseDurationButtonThree.setVisibility(View.INVISIBLE);
				decreaseDurationButtonThree.setVisibility(View.INVISIBLE);
				playListViewThree.setVisibility(View.INVISIBLE);						
			}
			if(i==3)
			{
				deleteSoundButtonFour.setVisibility(View.INVISIBLE);
				increaseDurationButtonFour.setVisibility(View.INVISIBLE);
				decreaseDurationButtonFour.setVisibility(View.INVISIBLE);
				playListViewFour.setVisibility(View.INVISIBLE);						
			}
			if(i==4)
			{
				deleteSoundButtonFive.setVisibility(View.INVISIBLE);
				increaseDurationButtonFive.setVisibility(View.INVISIBLE);
				decreaseDurationButtonFive.setVisibility(View.INVISIBLE);
				playListViewFive.setVisibility(View.INVISIBLE);						
			}
			if(i==5)
			{
				deleteSoundButtonSix.setVisibility(View.INVISIBLE);
				increaseDurationButtonSix.setVisibility(View.INVISIBLE);
				decreaseDurationButtonSix.setVisibility(View.INVISIBLE);
				playListViewSix.setVisibility(View.INVISIBLE);						
			}
			if(i==6)
			{
				deleteSoundButtonSeven.setVisibility(View.INVISIBLE);
				increaseDurationButtonSeven.setVisibility(View.INVISIBLE);
				decreaseDurationButtonSeven.setVisibility(View.INVISIBLE);
				playListViewSeven.setVisibility(View.INVISIBLE);						
			}
			if(i==7)
			{
				deleteSoundButtonEight.setVisibility(View.INVISIBLE);
				increaseDurationButtonEight.setVisibility(View.INVISIBLE);
				decreaseDurationButtonEight.setVisibility(View.INVISIBLE);
				playListViewEight.setVisibility(View.INVISIBLE);						
			}
		}
	}
}

