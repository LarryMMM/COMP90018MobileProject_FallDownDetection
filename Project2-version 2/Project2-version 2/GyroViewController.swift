//
//  GyroViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 30/9/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit
import CoreMotion

class GyroViewController: UIViewController {

    let manager = CMMotionManager()
    
    @IBOutlet var rotX: UILabel!
    @IBOutlet var rotY: UILabel!
    @IBOutlet var rotZ: UILabel!
    var rotX_measure : Double = 0
    var rotY_measure : Double = 0
    var rotZ_measure : Double = 0
    
    @IBAction func resetValues(){
        rotX_measure = 0
        rotY_measure = 0
        rotZ_measure = 0
    }
    func outputRotData(rotation: CMRotationRate) {
        rotX?.text = "\(round(rotation.x * 1000)/100) rad/s"
        rotY?.text = "\(round(rotation.y * 1000)/100) rad/s"
        rotZ?.text = "\(round(rotation.z * 1000)/100) rad/s"
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if manager.isGyroAvailable{
            manager.gyroUpdateInterval = 0.1
            manager.startGyroUpdates(to: OperationQueue.current!, withHandler: {
                (gyroData: CMGyroData? , NSError)->Void in
                self.outputRotData(rotation: gyroData!.rotationRate)
                if (NSError != nil){
                    print("\(String(describing: NSError))")
                }
            })
        }
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
