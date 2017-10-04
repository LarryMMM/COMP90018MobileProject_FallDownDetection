//
//  SignUpViewController.swift
//  Project2-version 2
//
//  Created by Jianqiang Zhang on 30/9/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit

class SignUpViewController: UIViewController {

    @IBOutlet var txtUserName: UITextField!
    @IBOutlet var txtPhoneNumber: UITextField!
    @IBOutlet var txtEmailAddress: UITextField!
    @IBOutlet var txtPassword: UITextField!
    @IBOutlet var txtPasswordConfirmation: UITextField!
    @IBOutlet var btnRegister: UIButton!
    
    
    
    
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
