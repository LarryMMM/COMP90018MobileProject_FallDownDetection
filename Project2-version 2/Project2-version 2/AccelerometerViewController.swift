//
//  AccelerometerViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 30/9/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit
import CoreMotion

class AccelerometerViewController: UIViewController {

    
    @IBOutlet var accX: UILabel!
    @IBOutlet var accY: UILabel!
    @IBOutlet var accZ: UILabel!
    var accX_measure : Double = 0
    var accY_measure : Double = 0
    var accZ_measure : Double = 0
    
    let manager = CMMotionManager()
    @IBAction func resetValues(){
        accX_measure = 0
        accY_measure = 0
        accZ_measure = 0
    }
    func outputAccData(acceleration: CMAcceleration) {
        accX?.text = "\(round(acceleration.x * 1000)/100)  m／s2"
        accY?.text = "\(round(acceleration.y * 1000)/100) m／s2"
        accZ?.text = "\(round(acceleration.z * 1000)/100) m／s2"
        
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
        self.resetValues()
        if manager.isAccelerometerAvailable{
            //print("success in accelerometer")
            manager.accelerometerUpdateInterval = 0.1 //time interval
            manager.startAccelerometerUpdates(to: OperationQueue.current!, withHandler: {
                (accelerometerData: CMAccelerometerData? , NSError)->Void in
                self.outputAccData(acceleration: accelerometerData!.acceleration)
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
