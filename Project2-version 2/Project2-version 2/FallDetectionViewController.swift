//
//  FallDetectionViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 1/10/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit


class FallDetectionViewController: UIViewController {

    @IBOutlet var slideToUnlock: UISlider!
    var UNLOCK : Bool = false
    
    @IBAction func startFallDetection(_ sender: Any) {
        if !UNLOCK {
            if self.slideToUnlock.value == 1 {
                self.slideToUnlock.isHidden = true
                self.UNLOCK = true
                
            }
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
