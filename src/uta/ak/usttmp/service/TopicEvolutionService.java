/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak.usttmp.service;

import java.util.List;
import uta.ak.usttmp.model.Topic;
import uta.ak.usttmp.model.EvolutionRelationship;

/**
 *
 * @author zhangcong
 */
public interface TopicEvolutionService {
    
    public List<EvolutionRelationship> getTopicEvolutionRelationships( List<Topic> preTopics,
                                                                            List<Topic> nextTopics);
        
    
    public double getSimilarity(Topic tp1, Topic tp2);
    
}
