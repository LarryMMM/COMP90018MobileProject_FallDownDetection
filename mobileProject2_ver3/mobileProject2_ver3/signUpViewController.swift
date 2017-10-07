//
//  signUpViewController.swift
//  mobileProject2_ver3
//
//  Created by Jianqiang Zhang on 5/10/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit

class signUpViewController: UIViewController,UITextFieldDelegate {

    @IBOutlet var txtUserName: UITextField!
    @IBOutlet var txtPhoneNumber: UITextField!
    @IBOutlet var txtEmailAddress: UITextField!
    @IBOutlet var txtPassword: UITextField!
    @IBOutlet var txtPasswordConfirmation: UITextField!
    @IBOutlet var btnRegister: UIButton!
    
    @IBAction func register(_ sender: UIButton) {
    //check data in cloud
        
    }
    @IBAction func editUserName(_ sender: Any) {
        txtUserName.text = ""
    }
    @IBAction func editPhoneNumber(_ sender: Any) {
        txtPhoneNumber.text = ""
    }
  
    @IBAction func editEmail(_ sender: Any) {
        txtEmailAddress.text = ""
    }
    @IBAction func editPasswd(_ sender: Any) {
        txtPassword.text = ""
    }
    
    @IBAction func editPasswdConfirm(_ sender: Any) {
        txtPasswordConfirmation.text = ""
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        let allowedCharacters = CharacterSet.decimalDigits
        //        let allowedCharacters = CharacterSet.letters
        let characterSet = CharacterSet(charactersIn: string)
        return allowedCharacters.isSuperset(of: characterSet)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        self.txtPhoneNumber.delegate = self //restrict to numbers
        // Do any additional setup after loading the view.
        btnRegister.isEnabled = false
        txtUserName.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        txtPhoneNumber.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        txtEmailAddress.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        txtPassword.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        txtPasswordConfirmation.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @objc func editingChanged(_ textField : UITextField) {
        if ((textField.text!.isEmpty) || (textField.text!.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines).isEmpty) || (textField.text!.characters.first == " ")) {
            textField.text = ""
            return
        }
        guard
            let name = txtUserName.text, !name.isEmpty,
            let email = txtEmailAddress.text, !email.isEmpty,
            let password = txtPassword.text, !password.isEmpty,
            let passwordConfirm = txtPasswordConfirmation.text, !passwordConfirm.isEmpty,
                password == passwordConfirm
            else {
                self.btnRegister.isEnabled = false
                return
            }
        self.btnRegister.isEnabled = true
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
