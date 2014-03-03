package com.neetomo.nico.tagcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import net.arnx.jsonic.JSON;

import java.io.IOException;

import com.neetomo.nico.model.NicoVideo;
import com.neetomo.nico.model.NicoTag;


public class TagCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Text outKey = new Text();
    private IntWritable outVal = new IntWritable(1);
    
    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        NicoVideo video = JSON.decode(value.toString(), NicoVideo.class);
        //ISO 8601 形式のYYYY-MMを取り出す
        String[] timeSplit = video.upload_time.split("[-T]");
        String yearMonth = timeSplit[0] + "-" + timeSplit[1];
        for (NicoTag t : video.tags) {
            //カテゴリタグは除く
            if (t.category != 1) {
                outKey.set(t.tag + "," + yearMonth);
                context.write(outKey, outVal);
            }
        }
    }
}

