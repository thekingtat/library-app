package com.l.notel.notel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by nglx on 26/7/15.
 */
public class DropCrumbActivity extends Activity{
    ImageButton cat_quote, cat_review, cat_poem, cat_haiku, cat_story, cat_fact, book1, book2, book3, ppublic, pprivate, return_crumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropcrumbs);
        Intent prevActivity = getIntent();
        String message = prevActivity.getExtras().getString("message");

        EditText crumbBox = (EditText) findViewById(R.id.crumb1);
        crumbBox.setText(message);

        cat_quote = (ImageButton) findViewById(R.id.cat_quote);
        cat_review = (ImageButton) findViewById(R.id.cat_review);
        cat_poem = (ImageButton) findViewById(R.id.cat_poem);
        cat_haiku = (ImageButton) findViewById(R.id.cat_haiku);
        cat_story = (ImageButton) findViewById(R.id.cat_story);
        cat_fact = (ImageButton) findViewById(R.id.cat_funfact);
        book1 = (ImageButton) findViewById(R.id.book1);
        book2 = (ImageButton) findViewById(R.id.book2);
        book3 = (ImageButton) findViewById(R.id.book3);
        ppublic = (ImageButton) findViewById(R.id.priv_public);
        pprivate = (ImageButton) findViewById(R.id.priv_private);
        return_crumb = (ImageButton) findViewById(R.id.return_crumb);
    }

    public void onClickCatQuote(View view) {
        cat_quote.setImageResource(R.drawable.quoteorange);
        cat_review.setImageResource(R.drawable.reviewblue);
        cat_poem.setImageResource(R.drawable.poemblue);
        cat_haiku.setImageResource(R.drawable.haikublue);
        cat_story.setImageResource(R.drawable.storyblue);
        cat_fact.setImageResource(R.drawable.factblue);
    }

    public void onClickCatFact(View view) {
        cat_quote.setImageResource(R.drawable.quoteblue);
        cat_review.setImageResource(R.drawable.reviewblue);
        cat_poem.setImageResource(R.drawable.poemblue);
        cat_haiku.setImageResource(R.drawable.haikublue);
        cat_story.setImageResource(R.drawable.storyblue);
        cat_fact.setImageResource(R.drawable.factorange);
    }

    public void onClickCatPoem(View view) {
        cat_quote.setImageResource(R.drawable.quoteblue);
        cat_review.setImageResource(R.drawable.reviewblue);
        cat_poem.setImageResource(R.drawable.poemorange);
        cat_haiku.setImageResource(R.drawable.haikublue);
        cat_story.setImageResource(R.drawable.storyblue);
        cat_fact.setImageResource(R.drawable.factblue);
    }

    public void onClickCatHaiku(View view) {
        cat_quote.setImageResource(R.drawable.quoteblue);
        cat_review.setImageResource(R.drawable.reviewblue);
        cat_poem.setImageResource(R.drawable.poemblue);
        cat_haiku.setImageResource(R.drawable.haikuorange);
        cat_story.setImageResource(R.drawable.storyblue);
        cat_fact.setImageResource(R.drawable.factblue);
    }

    public void onClickCatReview(View view) {
        cat_quote.setImageResource(R.drawable.quoteblue);
        cat_review.setImageResource(R.drawable.revieworange);
        cat_poem.setImageResource(R.drawable.poemblue);
        cat_haiku.setImageResource(R.drawable.haikublue);
        cat_story.setImageResource(R.drawable.storyblue);
        cat_fact.setImageResource(R.drawable.factblue);
    }

    public void onClickCatStory(View view) {
        cat_quote.setImageResource(R.drawable.quoteblue);
        cat_review.setImageResource(R.drawable.reviewblue);
        cat_poem.setImageResource(R.drawable.poemblue);
        cat_haiku.setImageResource(R.drawable.haikublue);
        cat_story.setImageResource(R.drawable.storyorange);
        cat_fact.setImageResource(R.drawable.factblue);
    }

    public void onClickBook1(View view) {
        book1.setImageResource(R.drawable.book1orange);
        book2.setImageResource(R.drawable.book2blue);
        book3.setImageResource(R.drawable.book3blue);
    }

    public void onClickBook2(View view) {
        book1.setImageResource(R.drawable.book1blue);
        book2.setImageResource(R.drawable.book2orange);
        book3.setImageResource(R.drawable.book3blue);
    }

    public void onClickBook3(View view) {
        book1.setImageResource(R.drawable.book1blue);
        book2.setImageResource(R.drawable.book2blue);
        book3.setImageResource(R.drawable.book3orange);
    }

    public void onClickPublic(View view) {
        ppublic.setImageResource(R.drawable.publicorange);
        pprivate.setImageResource(R.drawable.privateblue);
    }

    public void onClickPrivate(View view) {
        ppublic.setImageResource(R.drawable.publicblue);
        pprivate.setImageResource(R.drawable.privateorange);
    }

    public void onClickReturnCrumb(View view) {
        Intent goToMain = new Intent (this, MainPageActivity.class);
        goToMain.putExtra("message", "note created");

        Toast.makeText(this, "Crumb Dropped!", Toast.LENGTH_LONG);
        startActivity(goToMain);
    }
}
