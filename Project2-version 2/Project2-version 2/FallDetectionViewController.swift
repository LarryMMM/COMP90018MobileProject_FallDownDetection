//
//  FallDetectionViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 1/10/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit


class FallDetectionViewController: UIViewController {
    
    @IBAction func `switch`(_ sender: UISwitch) {
        if(sender.isOn == true){
            print("start detection")
        }else{
            print("detection ended")
        }
        
    }
    
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
